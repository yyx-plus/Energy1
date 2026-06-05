package com.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.PriceStrategyEntity;
import com.service.PriceStrategyService;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 分时电价策略管理（后台）
 */
@RestController
@RequestMapping("/priceStrategy")
public class PriceStrategyController {

    @Autowired
    private PriceStrategyService priceStrategyService;

    @GetMapping("/list")
    public R list(@RequestParam(required = false) Integer stationId) {
        EntityWrapper<PriceStrategyEntity> wrapper = new EntityWrapper<>();
        if (stationId != null) wrapper.eq("station_id", stationId);
        else wrapper.isNull("station_id"); // 全局默认策略
        wrapper.orderBy("period_type").orderBy("start_hour");
        return R.ok().put("data", priceStrategyService.selectList(wrapper));
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable Integer id) {
        return R.ok().put("data", priceStrategyService.selectById(id));
    }

    @PostMapping("/save")
    public R save(@RequestBody PriceStrategyEntity strategy) {
        strategy.setCreateTime(new Date());
        priceStrategyService.insert(strategy);
        return R.ok();
    }

    @PostMapping("/update")
    public R update(@RequestBody PriceStrategyEntity strategy) {
        priceStrategyService.updateById(strategy);
        return R.ok();
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        priceStrategyService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /** 获取24小时电价曲线（供前端图表使用） */
    @GetMapping("/curve")
    public R curve(@RequestParam(required = false) Integer stationId) {
        List<Map<String, Object>> curve = priceStrategyService.get24HourPriceCurve(stationId);
        return R.ok().put("data", curve);
    }
}
