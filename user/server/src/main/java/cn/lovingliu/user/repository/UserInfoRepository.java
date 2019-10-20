package cn.lovingliu.user.repository;

import cn.lovingliu.user.dataobject.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-19
 */
public interface UserInfoRepository extends JpaRepository<UserInfo,String> {
    UserInfo findByOpenid(String openId);
}
