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
 * 充电枪头
 */
@TableName("charging_gun")
public class ChargingGunEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    private Integer id;

    /** 充电桩ID */
    @TableField(value = "station_id")
    private Integer stationId;

    /** 枪头编号 */
    @TableField(value = "gun_no")
    private String gunNo;

    /** 枪头名称 */
    @TableField(value = "gun_name")
    private String gunName;

    /** 接口类型 1:国标交流 2:国标直流 3:欧标 4:特斯拉 */
    @TableField(value = "gun_type")
    private Integer gunType;

    /** 功率(kW) */
    @TableField(value = "power_kw")
    private Double powerKw;

    /** 状态 1:空闲 2:占用 3:故障 4:离线 */
    @TableField(value = "status")
    private Integer status;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    @TableField(value = "update_time")
    private Date updateTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getStationId() { return stationId; }
    public void setStationId(Integer stationId) { this.stationId = stationId; }
    public String getGunNo() { return gunNo; }
    public void setGunNo(String gunNo) { this.gunNo = gunNo; }
    public String getGunName() { return gunName; }
    public void setGunName(String gunName) { this.gunName = gunName; }
    public Integer getGunType() { return gunType; }
    public void setGunType(Integer gunType) { this.gunType = gunType; }
    public Double getPowerKw() { return powerKw; }
    public void setPowerKw(Double powerKw) { this.powerKw = powerKw; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
