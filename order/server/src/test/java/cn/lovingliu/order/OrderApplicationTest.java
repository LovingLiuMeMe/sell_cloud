package cn.lovingliu.order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderApplicationTest {

    @Autowired
    private AmqpTemplate amqpTemplate;
    /**
     * @Desc 发送消息测试
     * @Author LovingLiu
    */
    @Value("${rabbitmq.consumer.queue}")
    private String queue;
    @Test
    public void send(){
        amqpTemplate.convertAndSend(queue,"now"+new Date());
    }
}