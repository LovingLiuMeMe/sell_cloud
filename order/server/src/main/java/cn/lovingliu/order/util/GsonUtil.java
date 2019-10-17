package cn.lovingliu.order.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

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

    public static Object fromJson(String json,Class classType){
        try{
            Gson gson = new Gson();
            Object obj = gson.fromJson(json,classType);
            return obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static Object fromJson(String json, Type type){
        try{
            Gson gson = new Gson();
            Object obj = gson.fromJson(json,type);
            return obj;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
