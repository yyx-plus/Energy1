package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 预约锁桩 Redis 服务
 */
@Service
public class ReserveRedisService {

    private static final String RESERVE_KEY_PREFIX = "reserve:gun:";
    private static final String ORDER_EXPIRE_KEY_PREFIX = "reserve:order:";
    // 默认锁桩时长15分钟
    private static final long RESERVE_EXPIRE_MINUTES = 15;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 查询枪头是否被锁定
     */
    public boolean isGunLocked(Integer gunId) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(RESERVE_KEY_PREFIX + gunId));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取枪头锁定的订单ID
     */
    public String getLockedOrderId(Integer gunId) {
        try {
            return redisTemplate.opsForValue().get(RESERVE_KEY_PREFIX + gunId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取预约剩余秒数
     * @return 正数=剩余秒数, -1=key存在但无过期时间, null=Redis异常或key不存在
     */
    public Long getReserveExpireSeconds(Integer orderId) {
        try {
            Long expire = redisTemplate.getExpire(ORDER_EXPIRE_KEY_PREFIX + orderId, TimeUnit.SECONDS);
            // Spring Data Redis: -2=key不存在, -1=key存在但无过期时间
            if (expire == null || expire < 0) {
                return null;
            }
            return expire;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检查订单预约是否已过期
     */
    public boolean isOrderExpired(Integer orderId) {
        try {
            return !Boolean.TRUE.equals(redisTemplate.hasKey(ORDER_EXPIRE_KEY_PREFIX + orderId));
        } catch (Exception e) {
            return false; // Redis不可用时默认未过期，允许继续充电
        }
    }

    /**
     * 锁定枪头（预约时调用）
     */
    public boolean lockGun(Integer gunId, Integer orderId) {
        try {
            String key = RESERVE_KEY_PREFIX + gunId;
            Boolean success = redisTemplate.opsForValue()
                    .setIfAbsent(key, String.valueOf(orderId), RESERVE_EXPIRE_MINUTES, TimeUnit.MINUTES);
            if (Boolean.TRUE.equals(success)) {
                redisTemplate.opsForValue().set(
                        ORDER_EXPIRE_KEY_PREFIX + orderId,
                        String.valueOf(gunId),
                        RESERVE_EXPIRE_MINUTES, TimeUnit.MINUTES
                );
            }
            return Boolean.TRUE.equals(success);
        } catch (Exception e) {
            return true; // Redis不可用时默认允许锁定
        }
    }

    /**
     * 释放枪头锁（取消/完成时调用）
     */
    public void releaseGun(Integer gunId, Integer orderId) {
        try {
            redisTemplate.delete(RESERVE_KEY_PREFIX + gunId);
            redisTemplate.delete(ORDER_EXPIRE_KEY_PREFIX + orderId);
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }
}
