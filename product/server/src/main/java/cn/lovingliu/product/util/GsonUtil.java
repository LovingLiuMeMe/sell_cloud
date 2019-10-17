package cn.lovingliu.product.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-17
 */
public class GsonUtil {
    /**
     * @Desc 将对象转换成json字符串
     * @Author LovingLiu
    */
    public static String toJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }
}
