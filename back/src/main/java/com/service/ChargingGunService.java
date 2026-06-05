package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.entity.ChargingGunEntity;

import java.util.List;

public interface ChargingGunService extends IService<ChargingGunEntity> {
    List<ChargingGunEntity> getByStationId(Integer stationId);
}
