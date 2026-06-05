package com.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 分时电价策略
 */
@TableName("price_strategy")
public class PriceStrategyEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    private Integer id;

    /** 充电桩ID，NULL表示全局默认 */
    @TableField(value = "station_id")
    private Integer stationId;

    /** 策略名称 */
    @TableField(value = "strategy_name")
    private String strategyName;

    /** 时段类型 1:尖 2:峰 3:平 4:谷 */
    @TableField(value = "period_type")
    private Integer periodType;

    /** 开始小时(0-23) */
    @TableField(value = "start_hour")
    private Integer startHour;

    /** 结束小时(0-23) */
    @TableField(value = "end_hour")
    private Integer endHour;

    /** 电价(元/kWh) */
    @TableField(value = "electric_price")
    private Double electricPrice;

    /** 服务费(元/kWh) */
    @TableField(value = "service_price")
    private Double servicePrice;

    /** 是否启用 */
    @TableField(value = "is_active")
    private Integer isActive;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getStationId() { return stationId; }
    public void setStationId(Integer stationId) { this.stationId = stationId; }
    public String getStrategyName() { return strategyName; }
    public void setStrategyName(String strategyName) { this.strategyName = strategyName; }
    public Integer getPeriodType() { return periodType; }
    public void setPeriodType(Integer periodType) { this.periodType = periodType; }
    public Integer getStartHour() { return startHour; }
    public void setStartHour(Integer startHour) { this.startHour = startHour; }
    public Integer getEndHour() { return endHour; }
    public void setEndHour(Integer endHour) { this.endHour = endHour; }
    public Double getElectricPrice() { return electricPrice; }
    public void setElectricPrice(Double electricPrice) { this.electricPrice = electricPrice; }
    public Double getServicePrice() { return servicePrice; }
    public void setServicePrice(Double servicePrice) { this.servicePrice = servicePrice; }
    public Integer getIsActive() { return isActive; }
    public void setIsActive(Integer isActive) { this.isActive = isActive; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
