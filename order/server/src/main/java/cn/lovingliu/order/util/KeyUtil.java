package cn.lovingliu.order.util;

import java.util.Random;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
public class KeyUtil {
    public static synchronized String getUniqueKey() {
        Random random = new Random();
        Integer a = random.nextInt(900000) + 10000;// 生成6位随机数
        return System.currentTimeMillis() + String.valueOf(a);
    }
}
