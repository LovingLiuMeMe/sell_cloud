package cn.lovingliu.user.service;

import cn.lovingliu.user.dataobject.UserInfo;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-19
 */
public interface UserService {
    /**
     * @Desc 通过Openid 查询用户信息
     * @Author LovingLiu
    */
    UserInfo finfByOpenId(String openId);
}
