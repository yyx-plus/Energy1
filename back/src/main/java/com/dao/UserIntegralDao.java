package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.UserIntegralEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户积分 Dao
 */
public interface UserIntegralDao extends BaseMapper<UserIntegralEntity> {

    List<UserIntegralEntity> selectByYonghuId(@Param("yonghuId") Integer yonghuId);

    Integer sumIntegralByYonghuId(@Param("yonghuId") Integer yonghuId);
}
