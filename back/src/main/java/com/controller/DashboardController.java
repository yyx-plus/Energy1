package com.controller;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ChargingGunEntity;
import com.entity.ChongdianzhuangEntity;
import com.entity.ChongdianzhuangOrderEntity;
import com.service.ChargingGunService;
import com.service.ChongdianzhuangOrderService;
import com.service.ChongdianzhuangService;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 数据大屏接口（后台）
 */
@RestController
@RequestMapping("/admin/dashboard")
public class DashboardController {

    @Autowired
    private ChongdianzhuangService stationService;
    @Autowired
    private ChongdianzhuangOrderService orderService;
    @Autowired
    private ChargingGunService gunService;

    /**
     * 大屏概览KPI
     */
    @GetMapping("/overview")
    public R overview() {
        // 今日时间范围
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0);
        Date todayStart = cal.getTime();

        // 今日完成订单
        List<ChongdianzhuangOrderEntity> todayOrders = orderService.selectList(
                new EntityWrapper<ChongdianzhuangOrderEntity>()
                        .eq("chongdianzhuang_order_types", 104)
                        .ge("create_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(todayStart))
        );

        // 总充电量(kWh)
        double totalKwh = todayOrders.stream()
                .mapToDouble(o -> o.getChargeKwh() != null ? o.getChargeKwh() : 0).sum();

        // 今日营收
        double todayRevenue = todayOrders.stream()
                .mapToDouble(o -> o.getChongdianzhuangOrderTruePrice() != null ? o.getChongdianzhuangOrderTruePrice() : 0).sum();
        double todayElectricFee = todayOrders.stream()
                .mapToDouble(o -> o.getElectricFee() != null ? o.getElectricFee() : 0).sum();
        double todayServiceFee = todayOrders.stream()
                .mapToDouble(o -> o.getServiceFee() != null ? o.getServiceFee() : 0).sum();

        // 碳减排（每度电减少0.785kg CO2）
        double carbonReduction = Math.round(totalKwh * 0.785 * 100.0) / 100.0;

        // 站点统计
        int totalStations = stationService.selectCount(
                new EntityWrapper<ChongdianzhuangEntity>().eq("chongdianzhuang_delete", 1));
        int onlineStations = stationService.selectCount(
                new EntityWrapper<ChongdianzhuangEntity>()
                        .eq("chongdianzhuang_delete", 1).eq("shangxia_types", 1));

        // 枪头统计
        List<ChargingGunEntity> allGuns = gunService.selectList(null);
        long freeGuns = allGuns.stream().filter(g -> g.getStatus() != null && g.getStatus() == 1).count();
        long busyGuns = allGuns.stream().filter(g -> g.getStatus() != null && g.getStatus() == 2).count();
        long faultGuns = allGuns.stream().filter(g -> g.getStatus() != null && g.getStatus() == 3).count();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("todayServiceCount", todayOrders.size());
        data.put("totalChargeKwh", Math.round(totalKwh * 100.0) / 100.0);
        data.put("carbonReduction", carbonReduction);
        data.put("todayRevenue", Math.round(todayRevenue * 100.0) / 100.0);
        data.put("todayElectricFee", Math.round(todayElectricFee * 100.0) / 100.0);
        data.put("todayServiceFee", Math.round(todayServiceFee * 100.0) / 100.0);
        data.put("totalStations", totalStations);
        data.put("onlineStations", onlineStations);
        data.put("totalGuns", allGuns.size());
        data.put("freeGuns", freeGuns);
        data.put("busyGuns", busyGuns);
        data.put("faultGuns", faultGuns);
        return R.ok().put("data", data);
    }

    /**
     * 城市充电热力图数据（站点坐标+今日使用次数）
     */
    @GetMapping("/heatmap")
    public R heatmap() {
        List<ChongdianzhuangEntity> stations = stationService.selectList(
                new EntityWrapper<ChongdianzhuangEntity>()
                        .eq("shangxia_types", 1).eq("chongdianzhuang_delete", 1)
        );

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0);
        String todayStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());

        List<Map<String, Object>> result = new ArrayList<>();
        for (ChongdianzhuangEntity s : stations) {
            if (s.getLongitude() == null || s.getLatitude() == null) continue;
            int count = orderService.selectCount(
                    new EntityWrapper<ChongdianzhuangOrderEntity>()
                            .eq("chongdianzhuang_id", s.getId())
                            .ge("create_time", todayStr)
            );
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", s.getId());
            item.put("name", s.getChongdianzhuangName());
            item.put("longitude", s.getLongitude());
            item.put("latitude", s.getLatitude());
            item.put("count", count);
            item.put("weight", Math.min(count + 1, 10)); // 热力权重
            result.add(item);
        }
        return R.ok().put("data", result);
    }

    /**
     * 日报表
     */
    @GetMapping("/report/daily")
    public R dailyReport(@RequestParam(required = false) String date) {
        String targetDate = date != null ? date : new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String start = targetDate + " 00:00:00";
        String end = targetDate + " 23:59:59";

        List<ChongdianzhuangOrderEntity> orders = orderService.selectList(
                new EntityWrapper<ChongdianzhuangOrderEntity>()
                        .eq("chongdianzhuang_order_types", 104)
                        .ge("create_time", start).le("create_time", end)
        );

        double totalRevenue = orders.stream().mapToDouble(o -> o.getChongdianzhuangOrderTruePrice() != null ? o.getChongdianzhuangOrderTruePrice() : 0).sum();
        double electricFee = orders.stream().mapToDouble(o -> o.getElectricFee() != null ? o.getElectricFee() : 0).sum();
        double serviceFee = orders.stream().mapToDouble(o -> o.getServiceFee() != null ? o.getServiceFee() : 0).sum();
        double totalKwh = orders.stream().mapToDouble(o -> o.getChargeKwh() != null ? o.getChargeKwh() : 0).sum();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("date", targetDate);
        data.put("orderCount", orders.size());
        data.put("totalRevenue", Math.round(totalRevenue * 100.0) / 100.0);
        data.put("electricFee", Math.round(electricFee * 100.0) / 100.0);
        data.put("serviceFee", Math.round(serviceFee * 100.0) / 100.0);
        data.put("totalKwh", Math.round(totalKwh * 100.0) / 100.0);
        data.put("carbonReduction", Math.round(totalKwh * 0.785 * 100.0) / 100.0);
        return R.ok().put("data", data);
    }

    /**
     * 月报表（按天汇总）
     */
    @GetMapping("/report/monthly")
    public R monthlyReport(@RequestParam(required = false) String month) {
        String targetMonth = month != null ? month : new SimpleDateFormat("yyyy-MM").format(new Date());
        String start = targetMonth + "-01 00:00:00";
        // 计算月末
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(targetMonth.split("-")[0]), Integer.parseInt(targetMonth.split("-")[1]) - 1, 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String end = targetMonth + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)) + " 23:59:59";

        List<ChongdianzhuangOrderEntity> orders = orderService.selectList(
                new EntityWrapper<ChongdianzhuangOrderEntity>()
                        .eq("chongdianzhuang_order_types", 104)
                        .ge("create_time", start).le("create_time", end)
        );

        // 按天分组
        Map<String, Map<String, Object>> dayMap = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (ChongdianzhuangOrderEntity o : orders) {
            String day = sdf.format(o.getCreateTime());
            dayMap.computeIfAbsent(day, k -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("date", k); m.put("orderCount", 0);
                m.put("revenue", 0.0); m.put("electricFee", 0.0);
                m.put("serviceFee", 0.0); m.put("kwh", 0.0);
                return m;
            });
            Map<String, Object> dm = dayMap.get(day);
            dm.put("orderCount", (int) dm.get("orderCount") + 1);
            dm.put("revenue", round2((double) dm.get("revenue") + (o.getChongdianzhuangOrderTruePrice() != null ? o.getChongdianzhuangOrderTruePrice() : 0)));
            dm.put("electricFee", round2((double) dm.get("electricFee") + (o.getElectricFee() != null ? o.getElectricFee() : 0)));
            dm.put("serviceFee", round2((double) dm.get("serviceFee") + (o.getServiceFee() != null ? o.getServiceFee() : 0)));
            dm.put("kwh", round2((double) dm.get("kwh") + (o.getChargeKwh() != null ? o.getChargeKwh() : 0)));
        }

        double totalRevenue = orders.stream().mapToDouble(o -> o.getChongdianzhuangOrderTruePrice() != null ? o.getChongdianzhuangOrderTruePrice() : 0).sum();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("month", targetMonth);
        data.put("totalOrders", orders.size());
        data.put("totalRevenue", round2(totalRevenue));
        data.put("days", new ArrayList<>(dayMap.values()));
        return R.ok().put("data", data);
    }

    private double round2(double v) { return Math.round(v * 100.0) / 100.0; }
}
