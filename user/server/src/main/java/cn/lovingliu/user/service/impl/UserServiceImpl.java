package cn.lovingliu.user.service.impl;

import cn.lovingliu.user.dataobject.UserInfo;
import cn.lovingliu.user.repository.UserInfoRepository;
import cn.lovingliu.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-19
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserInfo finfByOpenId(String openId) {
        return userInfoRepository.findByOpenid(openId);
    }
}
