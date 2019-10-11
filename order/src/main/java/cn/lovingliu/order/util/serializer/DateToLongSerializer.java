package cn.lovingliu.order.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * @Author：LovingLiu
 * @Description: 实现Date转换成Long类型的数据 一般配合@JsonSerialize 使用
 * @Date：Created in 2019-10-11
 */
public class DateToLongSerializer extends JsonSerializer<Date> {

    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.getTime() / 1000);
    }
}
