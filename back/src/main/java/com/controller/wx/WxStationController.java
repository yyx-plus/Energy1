package com.controller.wx;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ChargingGunEntity;
import com.entity.ChongdianzhuangEntity;
import com.entity.DictionaryEntity;
import com.entity.view.ChongdianzhuangView;
import com.service.ChargingGunService;
import com.service.ChongdianzhuangService;
import com.service.DictionaryService;
import com.service.PriceStrategyService;
import com.service.ReserveRedisService;
import com.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 小程序 - 充电站接口
 */
@RestController
@RequestMapping("/wx/station")
public class WxStationController {

    private static final Logger logger = LoggerFactory.getLogger(WxStationController.class);

    @Autowired
    private ChongdianzhuangService chongdianzhuangService;
    @Autowired
    private ChargingGunService chargingGunService;
    @Autowired
    private PriceStrategyService priceStrategyService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private ReserveRedisService reserveRedisService;

    // 充电桩类型字典缓存（key=codeIndex, value=indexName）
    private Map<Integer, String> stationTypeMap = null;

    private Map<Integer, String> getStationTypeMap() {
        if (stationTypeMap == null) {
            stationTypeMap = new HashMap<>();
            List<DictionaryEntity> list = dictionaryService.selectList(
                    new EntityWrapper<DictionaryEntity>().eq("dic_code", "chongdianzhuang_types")
            );
            for (DictionaryEntity d : list) {
                stationTypeMap.put(d.getCodeIndex(), d.getIndexName());
            }
        }
        return stationTypeMap;
    }

    /**
     * 地图聚合数据 - 返回所有上架站点坐标+状态
     */
    @IgnoreAuth
    @GetMapping("/map")
    public R mapData() {
        List<ChongdianzhuangEntity> list = chongdianzhuangService.selectList(
                new EntityWrapper<ChongdianzhuangEntity>()
                        .eq("shangxia_types", 1)
                        .eq("chongdianzhuang_delete", 1)
        );
        List<Map<String, Object>> result = new ArrayList<>();
        for (ChongdianzhuangEntity s : list) {
            if (s.getLongitude() == null || s.getLatitude() == null) continue;
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", s.getId());
            item.put("name", s.getChongdianzhuangName());
            item.put("longitude", s.getLongitude());
            item.put("latitude", s.getLatitude());
            item.put("address", s.getAddress());
            item.put("status", s.getChongdianzhuangZhuangtaiTypes());
            item.put("price", s.getChongdianzhuangNewMoney());
            // 统计空闲枪数
            List<ChargingGunEntity> guns = chargingGunService.getByStationId(s.getId());
            long freeCount = guns.stream().filter(g -> g.getStatus() != null && g.getStatus() == 1).count();
            item.put("freeGunCount", freeCount);
            item.put("totalGunCount", guns.size());
            result.add(item);
        }
        return R.ok().put("data", result);
    }

    /**
     * 附近站点列表（支持多标签筛选）
     * @param longitude 当前经度
     * @param latitude  当前纬度
     * @param radius    搜索半径(km)，默认5km
     * @param isFastCharge 是否快充 0/1
     * @param isFreeParking 是否免费停车 0/1
     * @param keyword   关键词搜索
     */
    @IgnoreAuth
    @GetMapping("/list")
    public R nearbyList(
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double latitude,
            @RequestParam(defaultValue = "5") Double radius,
            @RequestParam(required = false) Integer chongdianzhuangTypes,
            @RequestParam(required = false) Integer isFastCharge,
            @RequestParam(required = false) Integer isFreeParking,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) {

        EntityWrapper<ChongdianzhuangEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("shangxia_types", 1);
        wrapper.eq("chongdianzhuang_delete", 1);

        if (chongdianzhuangTypes != null) wrapper.eq("chongdianzhuang_types", chongdianzhuangTypes);
        if (isFastCharge != null) wrapper.eq("is_fast_charge", isFastCharge);
        if (isFreeParking != null) wrapper.eq("is_free_parking", isFreeParking);
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.andNew().like("chongdianzhuang_name", keyword).or().like("address", keyword);
        }

        List<ChongdianzhuangEntity> all = chongdianzhuangService.selectList(wrapper);

        // 按距离过滤和排序
        List<Map<String, Object>> result = new ArrayList<>();
        for (ChongdianzhuangEntity s : all) {
            if (s.getLongitude() == null || s.getLatitude() == null) continue;
            double dist = (longitude != null && latitude != null)
                    ? calcDistance(latitude, longitude, s.getLatitude(), s.getLongitude())
                    : 0;
            if (longitude != null && latitude != null && dist > radius) continue;

            Map<String, Object> item = buildStationItem(s, dist);
            result.add(item);
        }

        // 按距离排序
        if (longitude != null && latitude != null) {
            result.sort(Comparator.comparingDouble(m -> (Double) m.get("distance")));
        }

        // 手动分页
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, result.size());
        List<Map<String, Object>> pageData = start < result.size() ? result.subList(start, end) : new ArrayList<>();

        Map<String, Object> resp = new HashMap<>();
        resp.put("list", pageData);
        resp.put("total", result.size());
        resp.put("page", page);
        resp.put("limit", limit);
        return R.ok().put("data", resp);
    }

    /**
     * 站点详情（含枪头实时状态）
     */
    @IgnoreAuth
    @GetMapping("/{id}")
    public R detail(@PathVariable Integer id) {
        ChongdianzhuangEntity station = chongdianzhuangService.selectById(id);
        if (station == null) return R.error(404, "站点不存在");

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", station.getId());
        data.put("name", station.getChongdianzhuangName());
        data.put("photo", station.getChongdianzhuangPhoto());
        data.put("address", station.getAddress());
        data.put("longitude", station.getLongitude());
        data.put("latitude", station.getLatitude());
        data.put("content", station.getChongdianzhuangContent());
        data.put("price", station.getChongdianzhuangNewMoney());
        data.put("status", station.getChongdianzhuangZhuangtaiTypes());

        // 枪头列表（含实时锁定状态）
        List<ChargingGunEntity> guns = chargingGunService.getByStationId(id);
        List<Map<String, Object>> gunList = new ArrayList<>();
        for (ChargingGunEntity gun : guns) {
            Map<String, Object> g = new LinkedHashMap<>();
            g.put("id", gun.getId());
            g.put("gunNo", gun.getGunNo());
            g.put("gunName", gun.getGunName());
            g.put("gunType", gun.getGunType());
            g.put("powerKw", gun.getPowerKw());
            // 优先以Redis锁定状态为准
            int realStatus = gun.getStatus() != null ? gun.getStatus() : 1;
            if (realStatus == 1 && reserveRedisService.isGunLocked(gun.getId())) {
                realStatus = 2; // 被预约锁定，显示占用
            }
            g.put("status", realStatus);
            g.put("statusName", getGunStatusName(realStatus));
            gunList.add(g);
        }
        data.put("guns", gunList);

        // 当前电价
        data.put("currentPrice", priceStrategyService.getCurrentPrice(id));

        return R.ok().put("data", data);
    }

    /**
     * 24小时电价曲线
     */
    @IgnoreAuth
    @GetMapping("/{id}/price")
    public R priceCurve(@PathVariable Integer id) {
        List<Map<String, Object>> curve = priceStrategyService.get24HourPriceCurve(id);
        return R.ok().put("data", curve);
    }

    // ---- 工具方法 ----

    private Map<String, Object> buildStationItem(ChongdianzhuangEntity s, double dist) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", s.getId());
        item.put("name", s.getChongdianzhuangName());
        item.put("photo", s.getChongdianzhuangPhoto());
        item.put("address", s.getAddress());
        item.put("longitude", s.getLongitude());
        item.put("latitude", s.getLatitude());
        item.put("price", s.getChongdianzhuangNewMoney());
        item.put("status", s.getChongdianzhuangZhuangtaiTypes());
        item.put("stationType", s.getChongdianzhuangTypes());
        item.put("stationTypeName", getStationTypeMap().getOrDefault(s.getChongdianzhuangTypes(), ""));
        item.put("distance", Math.round(dist * 10.0) / 10.0);
        item.put("distanceText", dist < 1 ? (int)(dist * 1000) + "m" : String.format("%.1fkm", dist));
        item.put("isFastCharge", s.getIsFastCharge() != null ? s.getIsFastCharge() : 0);
        item.put("isFreeParking", s.getIsFreeParking() != null ? s.getIsFreeParking() : 0);

        List<ChargingGunEntity> guns = chargingGunService.getByStationId(s.getId());
        long freeCount = guns.stream().filter(g -> g.getStatus() != null && g.getStatus() == 1
                && !reserveRedisService.isGunLocked(g.getId())).count();
        item.put("freeGunCount", freeCount);
        item.put("totalGunCount", guns.size());
        return item;
    }

    /** Haversine公式计算两点距离(km) */
    private double calcDistance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private String getGunStatusName(int status) {
        switch (status) {
            case 1: return "空闲";
            case 2: return "占用";
            case 3: return "故障";
            case 4: return "离线";
            default: return "未知";
        }
    }
}
