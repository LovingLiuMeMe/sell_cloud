package cn.lovingliu.order.message.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

/**
 * @Author：LovingLiu
 * @Description: 使用spring-cloud-stream 实现消息的消费和生产(目前只支持rabbitMQ 和 kafka)
 * @Date：Created in 2019-10-16
 */

public interface StreamOutputClient {
    String INPUT = "myInput"; // 定义outout 通道

    @Input(StreamOutputClient.INPUT)
    MessageChannel input();
}
