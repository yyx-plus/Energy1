package com.controller.wx;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ChargingGunEntity;
import com.entity.ChongdianzhuangEntity;
import com.service.ChargingGunService;
import com.service.ChongdianzhuangService;
import com.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 小程序 - 路径补能规划
 */
@RestController  //rest控制器，返回值自动转json
@RequestMapping("/wx/route")
public class WxRoutePlanController {

    private static final Logger logger = LoggerFactory.getLogger(WxRoutePlanController.class);

    @Autowired
    private ChongdianzhuangService stationService;
    @Autowired
    private ChargingGunService gunService;

    /**
     * 路径补能规划
     * body: {
     *   startLat, startLng,       // 当前位置
     *   endLat, endLng,           // 目的地
     *   remainRange,              // 当前续航里程(km)
     *   safeRange                 // 安全余量(km)，默认20
     * }
     */
    @IgnoreAuth
    @PostMapping("/plan")
    public R plan(@RequestBody Map<String, Object> params) {
        logger.info("路径规划请求: params={}", params);

        // 参数验证
        Double startLat = parseDouble(params.get("startLat"));
        Double startLng = parseDouble(params.get("startLng"));
        Double endLat = parseDouble(params.get("endLat"));
        Double endLng = parseDouble(params.get("endLng"));
        Double remainRange = parseDouble(params.getOrDefault("remainRange", "100"));
        Double safeRange = parseDouble(params.getOrDefault("safeRange", "20"));

        // 验证必填参数
        if (startLat == null || startLng == null) {
            logger.warn("参数错误：起始位置为空");
            return R.error(400, "请提供起始位置");
        }
        if (endLat == null || endLng == null) {
            logger.warn("参数错误：目的地为空");
            return R.error(400, "请提供目的地");
        }

        // 验证坐标范围
        if (!isValidLatitude(startLat) || !isValidLongitude(startLng)) {
            logger.warn("参数错误：起始坐标无效 lat={}, lng={}", startLat, startLng);
            return R.error(400, "起始位置坐标无效");
        }
        if (!isValidLatitude(endLat) || !isValidLongitude(endLng)) {
            logger.warn("参数错误：目的坐标无效 lat={}, lng={}", endLat, endLng);
            return R.error(400, "目的地坐标无效");
        }

        // 验证续航范围
        if (remainRange == null || remainRange <= 0 || remainRange > 1000) {
            logger.warn("参数错误：续航里程无效 range={}", remainRange);
            return R.error(400, "续航里程必须在 0-1000km 之间");
        }
        if (safeRange == null || safeRange < 0 || safeRange > 100) {
            safeRange = 20.0;
        }

        try {
            // 总直线距离
            double totalDist = calcDistance(startLat, startLng, endLat, endLng);
            logger.info("总直线距离: {}km", String.format("%.2f", totalDist));

            // 获取所有上架站点
            List<ChongdianzhuangEntity> allStations = stationService.selectList(
                    new EntityWrapper<ChongdianzhuangEntity>()
                            .eq("shangxia_types", 1)
                            .eq("chongdianzhuang_delete", 1)
            );
            logger.info("查询到上架充电站: {} 个", allStations.size());

            // 算法：找出在路径走廊内（偏离不超过5km）且在续航范围内的站点
            double corridorWidth = 5.0; // 路径走廊宽度(km)
            List<Map<String, Object>> candidates = new ArrayList<>();

            for (ChongdianzhuangEntity s : allStations) {
                if (s.getLongitude() == null || s.getLatitude() == null) continue;
                double distFromStart = calcDistance(startLat, startLng, s.getLatitude(), s.getLongitude());
                // 必须在续航范围内（留安全余量）
                if (distFromStart > remainRange - safeRange) continue;
                // 必须在路径走廊内
                double deviation = pointToLineDistance(startLat, startLng, endLat, endLng, s.getLatitude(), s.getLongitude());
                if (deviation > corridorWidth) continue;

                // 计算绕路距离
                double detour = distFromStart + calcDistance(s.getLatitude(), s.getLongitude(), endLat, endLng) - totalDist;

                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", s.getId());
                item.put("name", s.getChongdianzhuangName());
                item.put("address", s.getAddress());
                item.put("longitude", s.getLongitude());
                item.put("latitude", s.getLatitude());
                item.put("price", s.getChongdianzhuangNewMoney());
                item.put("distFromStart", Math.round(distFromStart * 10.0) / 10.0);
                item.put("detourKm", Math.round(detour * 10.0) / 10.0);
                item.put("deviation", Math.round(deviation * 10.0) / 10.0);

                // 空闲枪数
                List<ChargingGunEntity> guns = gunService.getByStationId(s.getId());
                long freeCount = guns.stream().filter(g -> g.getStatus() != null && g.getStatus() == 1).count();
                item.put("freeGunCount", freeCount);
                candidates.add(item);
            }

            logger.info("筛选出候选充电站: {} 个", candidates.size());

            // 按绕路距离排序，取前5个
            List<Map<String, Object>> recommended = new ArrayList<>();
            if (!candidates.isEmpty()) {
                candidates.sort(Comparator.comparingDouble(m -> (Double) m.get("detourKm")));
                recommended = candidates.subList(0, Math.min(5, candidates.size()));
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("totalDistance", Math.round(totalDist * 10.0) / 10.0);
            result.put("remainRange", remainRange);
            result.put("needCharge", totalDist > remainRange - safeRange);
            result.put("stations", recommended);

            logger.info("路径规划完成，推荐站点: {} 个", recommended.size());
            return R.ok().put("data", result);

        } catch (Exception e) {
            logger.error("路径规划异常", e);
            return R.error(500, "服务器内部错误");
        }
    }

    /**
     * 安全解析 Double，返回 null 如果解析失败
     */
    private Double parseDouble(Object value) {
        if (value == null) return null;
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 验证纬度是否有效（-90 ~ 90）
     */
    private boolean isValidLatitude(Double lat) {
        return lat != null && lat >= -90 && lat <= 90;
    }

    /**
     * 验证经度是否有效（-180 ~ 180）
     */
    private boolean isValidLongitude(Double lng) {
        return lng != null && lng >= -180 && lng <= 180;
    }

    /** Haversine公式 */
    private double calcDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    /** 点到线段的距离(km) */
    private double pointToLineDistance(double lat1, double lon1, double lat2, double lon2, double pLat, double pLon) {
        double dx = lon2 - lon1, dy = lat2 - lat1;
        if (dx == 0 && dy == 0) return calcDistance(lat1, lon1, pLat, pLon);
        double t = ((pLon - lon1) * dx + (pLat - lat1) * dy) / (dx * dx + dy * dy);
        t = Math.max(0, Math.min(1, t));
        double nearLon = lon1 + t * dx, nearLat = lat1 + t * dy;
        return calcDistance(pLat, pLon, nearLat, nearLon);
    }
}
