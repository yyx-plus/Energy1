package com.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ChargingGunEntity;
import com.service.ChargingGunService;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 充电枪头管理（后台）
 */
@RestController
@RequestMapping("/chargingGun")
public class ChargingGunController {

    @Autowired
    private ChargingGunService chargingGunService;

    @GetMapping("/list")
    public R list(@RequestParam(required = false) Integer stationId) {
        EntityWrapper<ChargingGunEntity> wrapper = new EntityWrapper<>();
        if (stationId != null) wrapper.eq("station_id", stationId);
        wrapper.orderBy("station_id").orderBy("gun_no");
        return R.ok().put("data", chargingGunService.selectList(wrapper));
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable Integer id) {
        return R.ok().put("data", chargingGunService.selectById(id));
    }

    @PostMapping("/save")
    public R save(@RequestBody ChargingGunEntity gun) {
        gun.setCreateTime(new Date());
        gun.setUpdateTime(new Date());
        chargingGunService.insert(gun);
        return R.ok();
    }

    @PostMapping("/update")
    public R update(@RequestBody ChargingGunEntity gun) {
        gun.setUpdateTime(new Date());
        chargingGunService.updateById(gun);
        return R.ok();
    }

    /** 模拟设备状态变更 */
    @PostMapping("/changeStatus")
    public R changeStatus(@RequestBody ChargingGunEntity gun) {
        ChargingGunEntity entity = new ChargingGunEntity();
        entity.setId(gun.getId());
        entity.setStatus(gun.getStatus());
        entity.setUpdateTime(new Date());
        chargingGunService.updateById(entity);
        return R.ok();
    }

    @PostMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        chargingGunService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}
