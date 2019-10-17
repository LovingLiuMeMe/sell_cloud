package cn.lovingliu.order.message;

import cn.lovingliu.order.message.stream.StreamOutputClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * @Author：LovingLiu
 * @Description: stream 技术实现消息接收端
 * @Date：Created in 2019-10-16
 */
@Slf4j
@Component
@EnableBinding(StreamOutputClient.class)
public class StreamReceiver{
    /**
     * @Desc 监听 生产者 生产的消息
     * @Author LovingLiu
    */
    @StreamListener(StreamOutputClient.INPUT)
    public void receiveInput(Object message){
        log.info("消费者 消费了一条信息=>{}",message);
    }
}
