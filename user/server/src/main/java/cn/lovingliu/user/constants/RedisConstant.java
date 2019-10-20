package cn.lovingliu.user.constants;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-20
 */
public interface RedisConstant {
    Integer EXPIRE = 7200;// 两个小时
    String TOKEN_PREFIX = "token_%s";
}
