package cn.lovingliu.order.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @Author：LovingLiu
 * @Description: 接收MQ消息（消费者）,使用SpringBoot的amqp
 * @Date：Created in 2019-10-16
 */
@Slf4j
@Component
public class MqReceiver {
    //1.@RabbitListener(queues = "queuename") 手动在rabbit控制台创建queue
    //2.@RabbitListener(queuesToDeclare = @Queue("queuename")) 自动创建（不和exchange绑定）
    //3.如下 自动创建且和exhange绑定
    @RabbitListener(
            bindings = @QueueBinding(
                value = @Queue(value = "${rabbitmq.consumer.queue}",durable = "true"),
                exchange = @Exchange(name="${rabbitmq.consumer.exchange}",durable = "true",type = "topic"),
                key = "${rabbitmq.consumer.routing-key}"
            )
    )
    @RabbitHandler
    public void receive(String message){
        log.info("MqReceiver=>{}",message);
    }
}
