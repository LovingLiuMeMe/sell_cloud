package cn.lovingliu.user.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-19
 */
@Data
@Entity
@DynamicUpdate
public class UserInfo {
    @Id
    private String id;
    private String username;
    private String password;

    private String openid;

    private Integer role;
}
