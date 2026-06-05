package com.controller.wx;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.TokenEntity;
import com.entity.YonghuEntity;
import com.service.TokenService;
import com.service.YonghuService;
import com.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 微信小程序认证接口
 */
@RestController
@RequestMapping("/wx/auth")
public class WxAuthController {

    private static final Logger logger = LoggerFactory.getLogger(WxAuthController.class);

    @Autowired
    private YonghuService yonghuService;
    @Autowired
    private TokenService tokenService;

    /**
     * 微信小程序登录
     * 前端传入 openid（实际项目中应由后端用code换取，此处模拟）
     * 若用户不存在则自动注册
     */
    @IgnoreAuth
    @PostMapping("/login")
    public R wxLogin(@RequestBody Map<String, Object> params) {
        String openid = String.valueOf(params.get("openid"));
        String nickName = String.valueOf(params.getOrDefault("nickName", "微信用户"));
        String avatarUrl = String.valueOf(params.getOrDefault("avatarUrl", ""));

        if (StringUtils.isBlank(openid) || "null".equals(openid)) {
            return R.error(400, "openid不能为空");
        }

        // 查找已有用户
        YonghuEntity yonghu = yonghuService.selectOne(
                new EntityWrapper<YonghuEntity>().eq("openid", openid)
        );

        if (yonghu == null) {
            // 自动注册
            yonghu = new YonghuEntity();
            yonghu.setOpenid(openid);
            yonghu.setUsername("wx_" + openid.substring(0, Math.min(8, openid.length())));
            yonghu.setPassword(UUID.randomUUID().toString().replace("-", "").substring(0, 8));
            yonghu.setYonghuName(nickName);
            yonghu.setYonghuPhoto(avatarUrl);
            yonghu.setNewMoney(0.0);
            yonghu.setTotalIntegral(0);
            yonghu.setCreateTime(new Date());
            yonghuService.insert(yonghu);
        } else {
            // 更新昵称头像
            yonghu.setYonghuName(nickName);
            if (StringUtils.isNotBlank(avatarUrl) && !"null".equals(avatarUrl)) {
                yonghu.setYonghuPhoto(avatarUrl);
            }
            yonghuService.updateById(yonghu);
        }

        // 生成Token
        String token = UUID.randomUUID().toString().replace("-", "");
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUserid(yonghu.getId());
        tokenEntity.setUsername(yonghu.getUsername());
        tokenEntity.setTablename("yonghu");
        tokenEntity.setRole("用户");
        tokenEntity.setToken(token);
        tokenEntity.setAddtime(new Date());
        // Token有效期7天
        tokenEntity.setExpiratedtime(new Date(System.currentTimeMillis() + 7 * 24 * 3600 * 1000L));

        // 删除旧token
        tokenService.delete(new EntityWrapper<TokenEntity>()
                .eq("userid", yonghu.getId()).eq("tablename", "yonghu"));
        tokenService.insert(tokenEntity);

        return R.ok().put("token", token).put("userInfo", yonghu);
    }

    /**
     * 手机号登录（账号密码方式，兼容原有用户）
     */
    @IgnoreAuth
    @PostMapping("/loginByPhone")
    public R loginByPhone(@RequestBody Map<String, Object> params) {
        String username = String.valueOf(params.get("username"));
        String password = String.valueOf(params.get("password"));

        YonghuEntity yonghu = yonghuService.selectOne(
                new EntityWrapper<YonghuEntity>()
                        .eq("username", username)
                        .eq("password", password)
        );
        if (yonghu == null) {
            return R.error(401, "账号或密码错误");
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUserid(yonghu.getId());
        tokenEntity.setUsername(yonghu.getUsername());
        tokenEntity.setTablename("yonghu");
        tokenEntity.setRole("用户");
        tokenEntity.setToken(token);
        tokenEntity.setAddtime(new Date());
        tokenEntity.setExpiratedtime(new Date(System.currentTimeMillis() + 7 * 24 * 3600 * 1000L));

        tokenService.delete(new EntityWrapper<TokenEntity>()
                .eq("userid", yonghu.getId()).eq("tablename", "yonghu"));
        tokenService.insert(tokenEntity);

        return R.ok().put("token", token).put("userInfo", yonghu);
    }

    /**
     * 注册
     */
    @IgnoreAuth
    @PostMapping("/register")
    public R register(@RequestBody YonghuEntity yonghu) {
        if (StringUtils.isBlank(yonghu.getUsername())) {
            return R.error(400, "用户名不能为空");
        }
        YonghuEntity exist = yonghuService.selectOne(
                new EntityWrapper<YonghuEntity>().eq("username", yonghu.getUsername())
        );
        if (exist != null) {
            return R.error(511, "用户名已存在");
        }
        yonghu.setNewMoney(0.0);
        yonghu.setTotalIntegral(0);
        yonghu.setCreateTime(new Date());
        yonghuService.insert(yonghu);
        return R.ok();
    }
}
