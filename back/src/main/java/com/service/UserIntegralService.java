package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.entity.UserIntegralEntity;

import java.util.List;

public interface UserIntegralService extends IService<UserIntegralEntity> {
    List<UserIntegralEntity> getByYonghuId(Integer yonghuId);
    /** 增加积分并更新用户总积分 */
    void addIntegral(Integer yonghuId, Integer integral, String source, Integer sourceType, Integer refId);
}
