package com.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.PriceStrategyDao;
import com.entity.PriceStrategyEntity;
import com.service.PriceStrategyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("priceStrategyService")
@Transactional
public class PriceStrategyServiceImpl extends ServiceImpl<PriceStrategyDao, PriceStrategyEntity> implements PriceStrategyService {

    @Override
    public List<PriceStrategyEntity> getByStationId(Integer stationId) {
        return baseMapper.selectByStationId(stationId);
    }

    @Override
    public List<Map<String, Object>> get24HourPriceCurve(Integer stationId) {
        List<PriceStrategyEntity> strategies = getByStationId(stationId);
        List<Map<String, Object>> curve = new ArrayList<>();
        String[] periodNames = {"", "尖", "峰", "平", "谷"};

        for (int hour = 0; hour < 24; hour++) {
            PriceStrategyEntity matched = findStrategyForHour(strategies, hour);
            Map<String, Object> point = new LinkedHashMap<>();
            point.put("hour", hour);
            point.put("label", hour + ":00");
            if (matched != null) {
                point.put("electricPrice", matched.getElectricPrice());
                point.put("servicePrice", matched.getServicePrice());
                point.put("totalPrice", matched.getElectricPrice() + matched.getServicePrice());
                point.put("periodType", matched.getPeriodType());
                point.put("periodName", periodNames[matched.getPeriodType()]);
            } else {
                // 默认平时段价格
                point.put("electricPrice", 0.65);
                point.put("servicePrice", 0.40);
                point.put("totalPrice", 1.05);
                point.put("periodType", 3);
                point.put("periodName", "平");
            }
            curve.add(point);
        }
        return curve;
    }

    @Override
    public PriceStrategyEntity getCurrentPrice(Integer stationId) {
        List<PriceStrategyEntity> strategies = getByStationId(stationId);
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return findStrategyForHour(strategies, currentHour);
    }

    private PriceStrategyEntity findStrategyForHour(List<PriceStrategyEntity> strategies, int hour) {
        for (PriceStrategyEntity s : strategies) {
            int start = s.getStartHour();
            int end = s.getEndHour();
            // 处理跨天时段（如23-6）
            if (start > end) {
                if (hour >= start || hour < end) return s;
            } else {
                if (hour >= start && hour < end) return s;
            }
        }
        return null;
    }
}
