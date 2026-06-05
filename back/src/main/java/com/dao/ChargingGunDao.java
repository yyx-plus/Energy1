package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.ChargingGunEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 充电枪头 Dao
 */
public interface ChargingGunDao extends BaseMapper<ChargingGunEntity> {

    List<ChargingGunEntity> selectByStationId(@Param("stationId") Integer stationId);
}
