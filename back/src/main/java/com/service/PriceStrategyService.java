package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.entity.PriceStrategyEntity;

import java.util.List;
import java.util.Map;

public interface PriceStrategyService extends IService<PriceStrategyEntity> {
    List<PriceStrategyEntity> getByStationId(Integer stationId);
    /** 获取24小时电价曲线（每小时对应的电价+服务费） */
    List<Map<String, Object>> get24HourPriceCurve(Integer stationId);
    /** 根据当前小时获取当前电价 */
    PriceStrategyEntity getCurrentPrice(Integer stationId);
}
