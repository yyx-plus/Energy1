package com.thread;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ChargingGunEntity;
import com.entity.ChongdianzhuangOrderEntity;
import com.service.ChargingGunService;
import com.service.ChongdianzhuangOrderService;
import com.service.ReserveRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 定时任务：扫描过期预约，自动取消并释放枪头
 */
@Component
public class ReserveExpireTask {

    private static final Logger logger = LoggerFactory.getLogger(ReserveExpireTask.class);

    @Autowired
    private ChongdianzhuangOrderService orderService;
    @Autowired
    private ChargingGunService gunService;
    @Autowired
    private ReserveRedisService reserveRedisService;

    /**
     * 每分钟扫描一次过期预约
     */
    @Scheduled(fixedDelay = 60000)
    public void cancelExpiredReserves() {
        // 查找状态为"已预约"且过期时间已过的订单
        List<ChongdianzhuangOrderEntity> expiredOrders = orderService.selectList(
                new EntityWrapper<ChongdianzhuangOrderEntity>()
                        .eq("chongdianzhuang_order_types", 101)
                        .isNotNull("reserve_expire_time")
                        .le("reserve_expire_time", new Date())
        );

        for (ChongdianzhuangOrderEntity order : expiredOrders) {
            logger.info("自动取消过期预约，orderId={}", order.getId());
            order.setChongdianzhuangOrderTypes(102); // 已取消
            orderService.updateById(order);

            // 释放Redis锁
            if (order.getGunId() != null) {
                reserveRedisService.releaseGun(order.getGunId(), order.getId());
                // 恢复枪头状态
                ChargingGunEntity gun = gunService.selectById(order.getGunId());
                if (gun != null && gun.getStatus() == 2) {
                    gun.setStatus(1);
                    gunService.updateById(gun);
                }
            }
        }

        if (!expiredOrders.isEmpty()) {
            logger.info("共取消 {} 个过期预约", expiredOrders.size());
        }
    }
}
