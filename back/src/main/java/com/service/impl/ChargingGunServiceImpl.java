package com.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.ChargingGunDao;
import com.entity.ChargingGunEntity;
import com.service.ChargingGunService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("chargingGunService")
@Transactional
public class ChargingGunServiceImpl extends ServiceImpl<ChargingGunDao, ChargingGunEntity> implements ChargingGunService {

    @Override
    public List<ChargingGunEntity> getByStationId(Integer stationId) {
        return baseMapper.selectByStationId(stationId);
    }
}
