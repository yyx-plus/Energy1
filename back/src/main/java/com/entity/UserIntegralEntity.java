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
 * 用户积分记录
 */
@TableName("user_integral")
public class UserIntegralEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    private Integer id;

    @TableField(value = "yonghu_id")
    private Integer yonghuId;

    /** 积分变动(正增负减) */
    @TableField(value = "integral")
    private Integer integral;

    /** 来源描述 */
    @TableField(value = "source")
    private String source;

    /** 来源类型 1:充电奖励 2:反馈审核 3:签到 4:消费抵扣 */
    @TableField(value = "source_type")
    private Integer sourceType;

    /** 关联业务ID */
    @TableField(value = "ref_id")
    private Integer refId;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getYonghuId() { return yonghuId; }
    public void setYonghuId(Integer yonghuId) { this.yonghuId = yonghuId; }
    public Integer getIntegral() { return integral; }
    public void setIntegral(Integer integral) { this.integral = integral; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public Integer getSourceType() { return sourceType; }
    public void setSourceType(Integer sourceType) { this.sourceType = sourceType; }
    public Integer getRefId() { return refId; }
    public void setRefId(Integer refId) { this.refId = refId; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
