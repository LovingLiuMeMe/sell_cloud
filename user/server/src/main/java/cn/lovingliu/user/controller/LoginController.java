package cn.lovingliu.user.controller;

import cn.lovingliu.user.common.ServerResponse;
import cn.lovingliu.user.constants.CookieConstant;
import cn.lovingliu.user.constants.RedisConstant;
import cn.lovingliu.user.dataobject.UserInfo;
import cn.lovingliu.user.enums.CommonStatusEnum;
import cn.lovingliu.user.service.UserService;
import cn.lovingliu.user.util.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-20
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/buyer")
    public ServerResponse buyer(@RequestParam("openid") String openid,
                                HttpServletResponse httpServletResponse){
        // 1.openid和数据库数据匹配
        UserInfo userInfo = userService.finfByOpenId(openid);
        if(userInfo == null){
            return ServerResponse.createByErrorCodeMessage(CommonStatusEnum.LOGIN_FAIL.getCode(),
                    CommonStatusEnum.LOGIN_FAIL.getMsg());
        }
        // 2.判断角色
        if(CommonStatusEnum.BUYER.getCode() != userInfo.getRole()){
            return ServerResponse.createByErrorCodeMessage(CommonStatusEnum.ROLE_ERROR.getCode(),
                    CommonStatusEnum.ROLE_ERROR.getMsg());
        }
        // 3.设置cookie
        CookieUtil.set(httpServletResponse,CookieConstant.OPENID,userInfo.getOpenid(),CookieConstant.EXPIRE);
        return ServerResponse.createBySuccess();
    }
    @GetMapping("/seller")
    public ServerResponse seller(@RequestParam("openid") String openid,
                                 HttpServletRequest httpServletRequest,
                                 HttpServletResponse httpServletResponse){
        // 判断是否已经登录（避免cookie重复更新,redis重复写入）
        Cookie cookie = CookieUtil.get(httpServletRequest,CookieConstant.TOKEN);
        if(cookie != null){
           String token =  cookie.getValue();
           String openId = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX,token));
           if(StringUtils.isNotBlank(openId)){
               return ServerResponse.createBySuccess("登录成功,无需重复登录");
           }
        }

        // 1.openid和数据库数据匹配
        UserInfo userInfo = userService.finfByOpenId(openid);
        if(userInfo == null){
            return ServerResponse.createByErrorCodeMessage(CommonStatusEnum.LOGIN_FAIL.getCode(),
                    CommonStatusEnum.LOGIN_FAIL.getMsg());
        }
        // 2.判断角色
        if(CommonStatusEnum.SELLER.getCode() != userInfo.getRole()){
            return ServerResponse.createByErrorCodeMessage(CommonStatusEnum.ROLE_ERROR.getCode(),
                    CommonStatusEnum.ROLE_ERROR.getMsg());
        }
        // 3.redis写入
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX,token),
                userInfo.getOpenid(),
                RedisConstant.EXPIRE,
                TimeUnit.SECONDS);
        // 4.设置cookie
        CookieUtil.set(httpServletResponse,CookieConstant.TOKEN,token,CookieConstant.EXPIRE);
        return ServerResponse.createBySuccess();
    }


}
