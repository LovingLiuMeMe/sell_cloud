package cn.lovingliu.user.constants;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-20
 */
public interface CookieConstant {
    /**
     * token 卖家端cookie key
    */
    String TOKEN = "token";
    /**
     * token 买家端cookie key
     */
    String OPENID = "openid";
    /**
     * 过期时间(单位:s)
    */
    Integer EXPIRE = 7200;
}
