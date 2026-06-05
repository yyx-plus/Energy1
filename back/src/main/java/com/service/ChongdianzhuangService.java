package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.ChongdianzhuangEntity;
import com.entity.view.ChongdianzhuangView;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import java.util.List;

/**
 * 充电桩 服务类
 */
public interface ChongdianzhuangService extends IService<ChongdianzhuangEntity> {

    /**
    * @param params 查询参数
    * @return 带分页的查询出来的数据
    */
     PageUtils queryPage(Map<String, Object> params);

     /**
      * 增强查询：支持距离排序和实时状态筛选
      * @param params 查询参数
      * @return 带分页的查询结果
      */
     PageUtils queryPageEnhanced(Map<String, Object> params);

     /**
      * 查询所有可用充电桩
      * @return 可用充电桩列表
      */
     List<ChongdianzhuangView> queryAvailableList();

}