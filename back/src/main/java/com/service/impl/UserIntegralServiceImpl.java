package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.UserIntegralDao;
import com.dao.YonghuDao;
import com.entity.UserIntegralEntity;
import com.entity.YonghuEntity;
import com.service.UserIntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("userIntegralService")
@Transactional
public class UserIntegralServiceImpl extends ServiceImpl<UserIntegralDao, UserIntegralEntity> implements UserIntegralService {

    @Autowired
    private YonghuDao yonghuDao;

    @Override
    public List<UserIntegralEntity> getByYonghuId(Integer yonghuId) {
        return baseMapper.selectByYonghuId(yonghuId);
    }

    @Override
    public void addIntegral(Integer yonghuId, Integer integral, String source, Integer sourceType, Integer refId) {
        // 记录积分流水
        UserIntegralEntity record = new UserIntegralEntity();
        record.setYonghuId(yonghuId);
        record.setIntegral(integral);
        record.setSource(source);
        record.setSourceType(sourceType);
        record.setRefId(refId);
        record.setCreateTime(new Date());
        baseMapper.insert(record);

        // 更新用户总积分
        YonghuEntity yonghu = yonghuDao.selectById(yonghuId);
        if (yonghu != null) {
            Integer current = yonghu.getTotalIntegral() == null ? 0 : yonghu.getTotalIntegral();
            yonghu.setTotalIntegral(current + integral);
            yonghuDao.updateById(yonghu);
        }
    }
}
