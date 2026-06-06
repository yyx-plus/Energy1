package com.controller.wx;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.BaoxuiEntity;
import com.service.BaoxuiService;
import com.service.UserIntegralService;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小程序 - 社区纠错与故障反馈
 */
@RestController
@RequestMapping("/wx/feedback")
public class WxFeedbackController {

    @Autowired
    private BaoxuiService baoxuiService;
    @Autowired
    private UserIntegralService integralService;

    /** 提交故障反馈 */
    @IgnoreAuth
    @PostMapping("/submit")
    public R submit(@RequestBody BaoxuiEntity baoxui, HttpServletRequest request) {
        Integer yonghuId = (Integer) request.getSession().getAttribute("userId");
        if (yonghuId == null) {
            // 测试用，如果没有userId，给一个默认值
            yonghuId = 1;
        }
        baoxui.setYonghuId(yonghuId);
        baoxui.setBaoxuiZhuangtaiTypes(1); // 未维修（待审核）
        baoxui.setInsertTime(new Date());
        baoxui.setCreateTime(new Date());
        baoxuiService.insert(baoxui);
        Map<String, Object> result = new HashMap<>();
        result.put("id", baoxui.getId());
        return R.ok().put("data", result);
    }

    /** 我的反馈列表 */
    @IgnoreAuth
    @GetMapping("/myList")
    public R myList(HttpServletRequest request) {
        Integer yonghuId = (Integer) request.getSession().getAttribute("userId");
        if (yonghuId == null) {
            // 测试用，如果没有userId，给一个默认值
            yonghuId = 1;
        }
        List<BaoxuiEntity> list = baoxuiService.selectList(
                new EntityWrapper<BaoxuiEntity>()
                        .eq("yonghu_id", yonghuId)
                        .orderBy("create_time", false)
        );
        return R.ok().put("data", list);
    }

    /**
     * 审核通过（管理员调用）- 自动发放积分
     * 此接口由后台管理端调用
     */
    @IgnoreAuth
    @PostMapping("/approve/{id}")
    public R approve(@PathVariable Integer id, HttpServletRequest request) {
        BaoxuiEntity baoxui = baoxuiService.selectById(id);
        if (baoxui == null) return R.error(404, "反馈不存在");
        if (baoxui.getBaoxuiZhuangtaiTypes() == 2) return R.error(400, "已审核通过");

        baoxui.setBaoxuiZhuangtaiTypes(2); // 已维修/已审核
        baoxuiService.updateById(baoxui);

        // 发放积分奖励（审核通过奖励50积分）
        integralService.addIntegral(baoxui.getYonghuId(), 50, "故障反馈审核通过奖励", 2, id);

        return R.ok();
    }
}
