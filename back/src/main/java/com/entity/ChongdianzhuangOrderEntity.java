package com.entity;

import com.annotation.ColumnInfo;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import java.util.*;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.utils.DateUtil;


/**
 * 充电桩预约
 *
 * @author 
 * @email
 */
@TableName("chongdianzhuang_order")
public class ChongdianzhuangOrderEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public ChongdianzhuangOrderEntity() {

	}

	public ChongdianzhuangOrderEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ColumnInfo(comment="主键",type="int(11)")
    @TableField(value = "id")

    private Integer id;


    /**
     * 充电桩
     */
    @ColumnInfo(comment="充电桩",type="int(11)")
    @TableField(value = "chongdianzhuang_id")

    private Integer chongdianzhuangId;


    /**
     * 用户
     */
    @ColumnInfo(comment="用户",type="int(11)")
    @TableField(value = "yonghu_id")

    private Integer yonghuId;


    /**
     * 充电小时
     */
    @ColumnInfo(comment="充电小时",type="int(11)")
    @TableField(value = "buy_number")

    private Integer buyNumber;


    /**
     * 预约时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="预约时间",type="timestamp")
    @TableField(value = "chongdianzhuang_order_time")

    private Date chongdianzhuangOrderTime;


    /**
     * 实付价格
     */
    @ColumnInfo(comment="实付价格",type="decimal(10,2)")
    @TableField(value = "chongdianzhuang_order_true_price")

    private Double chongdianzhuangOrderTruePrice;


    /**
     * 订单类型
     */
    @ColumnInfo(comment="订单类型",type="int(11)")
    @TableField(value = "chongdianzhuang_order_types")

    private Integer chongdianzhuangOrderTypes;


    /**
     * 枪头ID
     */
    @ColumnInfo(comment="枪头ID",type="int(11)")
    @TableField(value = "gun_id")

    private Integer gunId;


    /**
     * 开始充电时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    @ColumnInfo(comment="开始充电时间",type="timestamp")
    @TableField(value = "start_time")

    private Date startTime;


    /**
     * 结束充电时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    @ColumnInfo(comment="结束充电时间",type="timestamp")
    @TableField(value = "end_time")

    private Date endTime;


    /**
     * 充电度数(kWh)
     */
    @ColumnInfo(comment="充电度数",type="decimal(10,2)")
    @TableField(value = "charge_kwh")

    private Double chargeKwh;


    /**
     * 电费
     */
    @ColumnInfo(comment="电费",type="decimal(10,2)")
    @TableField(value = "electric_fee")

    private Double electricFee;


    /**
     * 服务费
     */
    @ColumnInfo(comment="服务费",type="decimal(10,2)")
    @TableField(value = "service_fee")

    private Double serviceFee;


    /**
     * 预约过期时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    @ColumnInfo(comment="预约过期时间",type="timestamp")
    @TableField(value = "reserve_expire_time")

    private Date reserveExpireTime;


    /**
     * 订单创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="订单创建时间",type="timestamp")
    @TableField(value = "insert_time",fill = FieldFill.INSERT)

    private Date insertTime;


    /**
     * 创建时间  listShow
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="创建时间",type="timestamp")
    @TableField(value = "create_time",fill = FieldFill.INSERT)

    private Date createTime;


    /**
	 * 获取：主键
	 */
    public Integer getId() {
        return id;
    }
    /**
	 * 设置：主键
	 */

    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 获取：充电桩
	 */
    public Integer getChongdianzhuangId() {
        return chongdianzhuangId;
    }
    /**
	 * 设置：充电桩
	 */

    public void setChongdianzhuangId(Integer chongdianzhuangId) {
        this.chongdianzhuangId = chongdianzhuangId;
    }
    /**
	 * 获取：用户
	 */
    public Integer getYonghuId() {
        return yonghuId;
    }
    /**
	 * 设置：用户
	 */

    public void setYonghuId(Integer yonghuId) {
        this.yonghuId = yonghuId;
    }
    /**
	 * 获取：充电小时
	 */
    public Integer getBuyNumber() {
        return buyNumber;
    }
    /**
	 * 设置：充电小时
	 */

    public void setBuyNumber(Integer buyNumber) {
        this.buyNumber = buyNumber;
    }
    /**
	 * 获取：预约时间
	 */
    public Date getChongdianzhuangOrderTime() {
        return chongdianzhuangOrderTime;
    }
    /**
	 * 设置：预约时间
	 */

    public void setChongdianzhuangOrderTime(Date chongdianzhuangOrderTime) {
        this.chongdianzhuangOrderTime = chongdianzhuangOrderTime;
    }
    /**
	 * 获取：实付价格
	 */
    public Double getChongdianzhuangOrderTruePrice() {
        return chongdianzhuangOrderTruePrice;
    }
    /**
	 * 设置：实付价格
	 */

    public void setChongdianzhuangOrderTruePrice(Double chongdianzhuangOrderTruePrice) {
        this.chongdianzhuangOrderTruePrice = chongdianzhuangOrderTruePrice;
    }
    public Integer getChongdianzhuangOrderTypes() { return chongdianzhuangOrderTypes; }
    public void setChongdianzhuangOrderTypes(Integer chongdianzhuangOrderTypes) { this.chongdianzhuangOrderTypes = chongdianzhuangOrderTypes; }
    public Integer getGunId() { return gunId; }
    public void setGunId(Integer gunId) { this.gunId = gunId; }
    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }
    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }
    public Double getChargeKwh() { return chargeKwh; }
    public void setChargeKwh(Double chargeKwh) { this.chargeKwh = chargeKwh; }
    public Double getElectricFee() { return electricFee; }
    public void setElectricFee(Double electricFee) { this.electricFee = electricFee; }
    public Double getServiceFee() { return serviceFee; }
    public void setServiceFee(Double serviceFee) { this.serviceFee = serviceFee; }
    public Date getReserveExpireTime() { return reserveExpireTime; }
    public void setReserveExpireTime(Date reserveExpireTime) { this.reserveExpireTime = reserveExpireTime; }
    /**
	 * 获取：订单创建时间
	 */
    public Date getInsertTime() {
        return insertTime;
    }
    /**
	 * 设置：订单创建时间
	 */

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    /**
	 * 获取：创建时间  listShow
	 */
    public Date getCreateTime() {
        return createTime;
    }
    /**
	 * 设置：创建时间  listShow
	 */

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ChongdianzhuangOrder{" +
            ", id=" + id +
            ", chongdianzhuangId=" + chongdianzhuangId +
            ", yonghuId=" + yonghuId +
            ", buyNumber=" + buyNumber +
            ", chongdianzhuangOrderTime=" + DateUtil.convertString(chongdianzhuangOrderTime,"yyyy-MM-dd") +
            ", chongdianzhuangOrderTruePrice=" + chongdianzhuangOrderTruePrice +
            ", chongdianzhuangOrderTypes=" + chongdianzhuangOrderTypes +
            ", insertTime=" + DateUtil.convertString(insertTime,"yyyy-MM-dd") +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
        "}";
    }
}
