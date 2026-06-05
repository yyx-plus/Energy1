package com.controller.wx;

import com.entity.UserIntegralEntity;
import com.entity.YonghuEntity;
import com.service.UserIntegralService;
import com.service.YonghuService;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小程序 - 用户中心
 */
@RestController
@RequestMapping("/wx/user")
public class WxUserController {

    @Autowired
    private YonghuService yonghuService;
    @Autowired
    private UserIntegralService integralService;

    /** 获取个人信息 */
    @GetMapping("/info")
    public R info(HttpServletRequest request) {
        Integer yonghuId = (Integer) request.getSession().getAttribute("userId");
        YonghuEntity yonghu = yonghuService.selectById(yonghuId);
        if (yonghu == null) return R.error(404, "用户不存在");
        yonghu.setPassword(null); // 不返回密码
        return R.ok().put("data", yonghu);
    }

    /** 更新个人信息 */
    @PutMapping("/update")
    public R update(@RequestBody YonghuEntity yonghu, HttpServletRequest request) {
        Integer yonghuId = (Integer) request.getSession().getAttribute("userId");
        yonghu.setId(yonghuId);
        yonghu.setPassword(null); // 不允许通过此接口改密码
        yonghu.setNewMoney(null); // 不允许直接改余额
        yonghuService.updateById(yonghu);
        return R.ok();
    }

    /** 积分明细 */
    @GetMapping("/integral")
    public R integral(HttpServletRequest request) {
        Integer yonghuId = (Integer) request.getSession().getAttribute("userId");
        List<UserIntegralEntity> list = integralService.getByYonghuId(yonghuId);
        YonghuEntity yonghu = yonghuService.selectById(yonghuId);
        Map<String, Object> integralData = new HashMap<>();
        integralData.put("list", list);
        integralData.put("total", yonghu != null && yonghu.getTotalIntegral() != null ? yonghu.getTotalIntegral() : 0);
        return R.ok().put("data", integralData);
    }

    /** 模拟充值余额 */
    @PostMapping("/recharge")
    public R recharge(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Integer yonghuId = (Integer) request.getSession().getAttribute("userId");
        Double amount = Double.valueOf(String.valueOf(params.get("amount")));
        if (amount <= 0 || amount > 10000) return R.error(400, "充值金额不合法");

        YonghuEntity yonghu = yonghuService.selectById(yonghuId);
        if (yonghu == null) return R.error(404, "用户不存在");
        double current = yonghu.getNewMoney() != null ? yonghu.getNewMoney() : 0;
        yonghu.setNewMoney(Math.round((current + amount) * 100.0) / 100.0);
        yonghuService.updateById(yonghu);
        Map<String, Object> rechargeResult = new HashMap<>();
        rechargeResult.put("newBalance", yonghu.getNewMoney());
        return R.ok().put("data", rechargeResult);
    }
}
