package com.controller.wx;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.*;
import com.service.*;
import com.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 小程序 - 充电订单接口
 * 订单状态: 101已预约 102已取消 103充电中 104已完成
 */
@RestController
@RequestMapping("/wx/order")
public class WxOrderController {

    private static final Logger logger = LoggerFactory.getLogger(WxOrderController.class);

    @Autowired
    private ChongdianzhuangOrderService orderService;
    @Autowired
    private ChongdianzhuangService stationService;
    @Autowired
    private ChargingGunService gunService;
    @Autowired
    private YonghuService yonghuService;
    @Autowired
    private PriceStrategyService priceStrategyService;
    @Autowired
    private ReserveRedisService reserveRedisService;
    @Autowired
    private UserIntegralService integralService;

    /**
     * 预约锁桩
     * body: { stationId, gunId, reserveTime }
     */
    @PostMapping("/reserve")
    public R reserve(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Integer yonghuId = (Integer) request.getSession().getAttribute("userId");
        Integer stationId = Integer.valueOf(String.valueOf(params.get("stationId")));
        Integer gunId = Integer.valueOf(String.valueOf(params.get("gunId")));

        // 检查枪头是否已被锁定
        if (reserveRedisService.isGunLocked(gunId)) {
            return R.error(409, "该枪头已被预约，请选择其他枪头");
        }

        ChongdianzhuangEntity station = stationService.selectById(stationId);
        if (station == null) return R.error(404, "站点不存在");

        // 创建预约订单
        ChongdianzhuangOrderEntity order = new ChongdianzhuangOrderEntity();
        order.setChongdianzhuangId(stationId);
        order.setGunId(gunId);
        order.setYonghuId(yonghuId);
        order.setBuyNumber(0);
        order.setChongdianzhuangOrderTime(new Date());
        order.setChongdianzhuangOrderTruePrice(0.0);
        order.setChongdianzhuangOrderTypes(101); // 已预约
        order.setInsertTime(new Date());
        order.setCreateTime(new Date());
        // 预约过期时间 = 当前时间 + 15分钟
        order.setReserveExpireTime(new Date(System.currentTimeMillis() + 15 * 60 * 1000L));
        orderService.insert(order);

        // Redis锁桩
        boolean locked = reserveRedisService.lockGun(gunId, order.getId());
        if (!locked) {
            // 并发情况下锁失败，回滚订单
            order.setChongdianzhuangOrderTypes(102);
            orderService.updateById(order);
            return R.error(409, "锁桩失败，请重试");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", order.getId());
        result.put("expireSeconds", 15 * 60);
        result.put("expireTime", order.getReserveExpireTime());
        return R.ok().put("data", result);
    }

    /**
     * 取消预约
     */
    @DeleteMapping("/{orderId}/cancel")
    public R cancel(@PathVariable Integer orderId, HttpServletRequest request) {
        Integer yonghuId = (Integer) request.getSession().getAttribute("userId");
        ChongdianzhuangOrderEntity order = orderService.selectById(orderId);
        if (order == null) return R.error(404, "订单不存在");
        if (!order.getYonghuId().equals(yonghuId)) return R.error(403, "无权操作");
        if (order.getChongdianzhuangOrderTypes() != 101) return R.error(400, "订单状态不允许取消");

        order.setChongdianzhuangOrderTypes(102);
        orderService.updateById(order);

        // 释放Redis锁（通过orderId找gunId）
        if (order.getGunId() != null) {
            reserveRedisService.releaseGun(order.getGunId(), orderId);
            // 恢复枪头状态为空闲
            ChargingGunEntity gun = gunService.selectById(order.getGunId());
            if (gun != null) { gun.setStatus(1); gunService.updateById(gun); }
        }
        return R.ok();
    }

    /**
     * 开始充电（模拟插枪）
     */
    @PostMapping("/startCharge")
    public R startCharge(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Integer yonghuId = (Integer) request.getSession().getAttribute("userId");
        Integer orderId = Integer.valueOf(String.valueOf(params.get("orderId")));

        ChongdianzhuangOrderEntity order = orderService.selectById(orderId);
        if (order == null) return R.error(404, "订单不存在");
        if (!order.getYonghuId().equals(yonghuId)) return R.error(403, "无权操作");
        logger.info("开始充电请求: orderId={}, userId={}, 当前订单状态={}", orderId, yonghuId, order.getChongdianzhuangOrderTypes());
        if (order.getChongdianzhuangOrderTypes() == null || order.getChongdianzhuangOrderTypes() != 101) {
            String statusDesc = order.getChongdianzhuangOrderTypes() == null ? "null" :
                (order.getChongdianzhuangOrderTypes() == 102 ? "已取消" :
                 order.getChongdianzhuangOrderTypes() == 103 ? "充电中" :
                 order.getChongdianzhuangOrderTypes() == 104 ? "已完成" : String.valueOf(order.getChongdianzhuangOrderTypes()));
            return R.error(400, "订单状态异常，当前状态：" + statusDesc);
        }

        // 检查预约是否过期：优先用Redis，Redis无记录时降级用数据库字段
        boolean expired = false;
        Long remainSeconds = reserveRedisService.getReserveExpireSeconds(orderId);
        if (remainSeconds != null && remainSeconds > 0) {
            // Redis有记录且未过期，允许充电
            expired = false;
            logger.info("Redis预约未过期，orderId={}, 剩余秒数={}", orderId, remainSeconds);
        } else {
            // Redis无记录或异常，降级用数据库字段
            if (order.getReserveExpireTime() != null) {
                expired = order.getReserveExpireTime().before(new Date());
                logger.info("Redis无记录，使用数据库时间判断: orderId={}, reserveExpireTime={}, 当前时间={}, 是否过期={}",
                    orderId, order.getReserveExpireTime(), new Date(), expired);
            } else {
                logger.warn("订单无预约过期时间，orderId={}", orderId);
            }
        }
        if (expired) {
            order.setChongdianzhuangOrderTypes(102);
            orderService.updateById(order);
            return R.error(410, "预约已过期，请重新预约");
        }

        order.setChongdianzhuangOrderTypes(103); // 充电中
        order.setStartTime(new Date());
        orderService.updateById(order);

        // 更新枪头状态为占用
        if (order.getGunId() != null) {
            ChargingGunEntity gun = gunService.selectById(order.getGunId());
            if (gun != null) { gun.setStatus(2); gunService.updateById(gun); }
        }

        Map<String, Object> startResult = new HashMap<>();
        startResult.put("startTime", order.getStartTime());
        return R.ok().put("data", startResult);
    }

    /**
     * 结束充电（计算费用）
     */
    @PostMapping("/stopCharge")
    public R stopCharge(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Integer yonghuId = (Integer) request.getSession().getAttribute("userId");
        Integer orderId = Integer.valueOf(String.valueOf(params.get("orderId")));
        Double chargeKwh = Double.valueOf(String.valueOf(params.getOrDefault("chargeKwh", "0")));

        ChongdianzhuangOrderEntity order = orderService.selectById(orderId);
        if (order == null) return R.error(404, "订单不存在");
        if (!order.getYonghuId().equals(yonghuId)) return R.error(403, "无权操作");
        if (order.getChongdianzhuangOrderTypes() != 103) return R.error(400, "订单未在充电中");

        Date endTime = new Date();
        order.setEndTime(endTime);
        order.setChargeKwh(chargeKwh);

        // 计算费用
        PriceStrategyEntity price = priceStrategyService.getCurrentPrice(order.getChongdianzhuangId());
        double electricFee, serviceFee;
        if (price != null) {
            electricFee = Math.round(chargeKwh * price.getElectricPrice() * 100.0) / 100.0;
            serviceFee = Math.round(chargeKwh * price.getServicePrice() * 100.0) / 100.0;
        } else {
            // 按小时计费兜底
            ChongdianzhuangEntity station = stationService.selectById(order.getChongdianzhuangId());
            double hours = order.getStartTime() != null
                    ? (endTime.getTime() - order.getStartTime().getTime()) / 3600000.0 : 0;
            electricFee = Math.round(hours * (station != null ? station.getChongdianzhuangNewMoney() : 1.0) * 100.0) / 100.0;
            serviceFee = 0;
        }

        order.setElectricFee(electricFee);
        order.setServiceFee(serviceFee);
        order.setChongdianzhuangOrderTruePrice(electricFee + serviceFee);
        order.setChongdianzhuangOrderTypes(104); // 已完成（待支付）
        orderService.updateById(order);

        // 释放枪头
        if (order.getGunId() != null) {
            reserveRedisService.releaseGun(order.getGunId(), orderId);
            ChargingGunEntity gun = gunService.selectById(order.getGunId());
            if (gun != null) { gun.setStatus(1); gunService.updateById(gun); }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("chargeKwh", chargeKwh);
        result.put("electricFee", electricFee);
        result.put("serviceFee", serviceFee);
        result.put("totalFee", electricFee + serviceFee);
        return R.ok().put("data", result);
    }

    /**
     * 支付（余额扣款）
     */
    @PostMapping("/pay")
    public R pay(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Integer yonghuId = (Integer) request.getSession().getAttribute("userId");
        Integer orderId = Integer.valueOf(String.valueOf(params.get("orderId")));

        ChongdianzhuangOrderEntity order = orderService.selectById(orderId);
        if (order == null) return R.error(404, "订单不存在");
        if (!order.getYonghuId().equals(yonghuId)) return R.error(403, "无权操作");
        if (order.getChongdianzhuangOrderTypes() != 104) return R.error(400, "订单状态不允许支付");

        YonghuEntity yonghu = yonghuService.selectById(yonghuId);
        double totalFee = order.getChongdianzhuangOrderTruePrice() != null ? order.getChongdianzhuangOrderTruePrice() : 0;
        if (yonghu.getNewMoney() == null || yonghu.getNewMoney() < totalFee) {
            return R.error(402, "余额不足，请先充值");
        }

        // 扣款
        yonghu.setNewMoney(Math.round((yonghu.getNewMoney() - totalFee) * 100.0) / 100.0);
        yonghuService.updateById(yonghu);

        // 充电奖励积分（每度电1积分）
        int earnIntegral = (int) Math.floor(order.getChargeKwh() != null ? order.getChargeKwh() : 0);
        if (earnIntegral > 0) {
            integralService.addIntegral(yonghuId, earnIntegral, "充电奖励", 1, orderId);
        }

        Map<String, Object> payResult = new HashMap<>();
        payResult.put("paidFee", totalFee);
        payResult.put("remainMoney", yonghu.getNewMoney());
        payResult.put("earnIntegral", earnIntegral);
        return R.ok().put("data", payResult);
    }

    /**
     * 我的订单列表
     */
    @GetMapping("/list")
    public R myOrders(@RequestParam(required = false) Integer status,
                      @RequestParam(defaultValue = "1") Integer page,
                      @RequestParam(defaultValue = "10") Integer limit,
                      HttpServletRequest request) {
        Integer yonghuId = (Integer) request.getSession().getAttribute("userId");

        EntityWrapper<ChongdianzhuangOrderEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("yonghu_id", yonghuId);
        if (status != null) wrapper.eq("chongdianzhuang_order_types", status);
        wrapper.orderBy("create_time", false);

        List<ChongdianzhuangOrderEntity> all = orderService.selectList(wrapper);

        // 手动分页
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, all.size());
        List<ChongdianzhuangOrderEntity> pageData = start < all.size() ? all.subList(start, end) : new ArrayList<>();

        // 补充站点名称
        List<Map<String, Object>> result = new ArrayList<>();
        for (ChongdianzhuangOrderEntity o : pageData) {
            Map<String, Object> item = orderToMap(o);
            result.add(item);
        }

        Map<String, Object> listResp = new HashMap<>();
        listResp.put("list", result);
        listResp.put("total", all.size());
        return R.ok().put("data", listResp);
    }

    /**
     * 订单详情
     */
    @GetMapping("/{orderId}")
    public R orderDetail(@PathVariable Integer orderId, HttpServletRequest request) {
        Integer yonghuId = (Integer) request.getSession().getAttribute("userId");
        ChongdianzhuangOrderEntity order = orderService.selectById(orderId);
        if (order == null) return R.error(404, "订单不存在");
        if (!order.getYonghuId().equals(yonghuId)) return R.error(403, "无权查看");

        Map<String, Object> data = orderToMap(order);
        // 补充预约剩余时间
        if (order.getChongdianzhuangOrderTypes() == 101) {
            Long remain = reserveRedisService.getReserveExpireSeconds(orderId);
            data.put("reserveRemainSeconds", remain != null && remain > 0 ? remain : 0);
        }
        return R.ok().put("data", data);
    }

    private Map<String, Object> orderToMap(ChongdianzhuangOrderEntity o) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("id", o.getId());
        item.put("stationId", o.getChongdianzhuangId());
        item.put("gunId", o.getGunId());
        item.put("orderTime", o.getChongdianzhuangOrderTime());
        item.put("startTime", o.getStartTime());
        item.put("endTime", o.getEndTime());
        item.put("chargeKwh", o.getChargeKwh());
        item.put("electricFee", o.getElectricFee());
        item.put("serviceFee", o.getServiceFee());
        item.put("totalFee", o.getChongdianzhuangOrderTruePrice());
        item.put("status", o.getChongdianzhuangOrderTypes());
        item.put("statusName", getOrderStatusName(o.getChongdianzhuangOrderTypes()));
        item.put("createTime", o.getCreateTime());
        // 补充站点名称
        ChongdianzhuangEntity station = stationService.selectById(o.getChongdianzhuangId());
        if (station != null) item.put("stationName", station.getChongdianzhuangName());
        return item;
    }

    private String getOrderStatusName(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 101: return "已预约";
            case 102: return "已取消";
            case 103: return "充电中";
            case 104: return "已完成";
            default: return "未知";
        }
    }
}
