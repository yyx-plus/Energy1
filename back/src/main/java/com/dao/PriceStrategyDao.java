package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.PriceStrategyEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分时电价策略 Dao
 */
public interface PriceStrategyDao extends BaseMapper<PriceStrategyEntity> {

    List<PriceStrategyEntity> selectByStationId(@Param("stationId") Integer stationId);
}
