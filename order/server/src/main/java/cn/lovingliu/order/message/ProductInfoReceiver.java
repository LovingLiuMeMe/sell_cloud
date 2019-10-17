package cn.lovingliu.order.message;

import cn.lovingliu.order.util.GsonUtil;
import cn.lovingliu.product.common.ProductInfoOutput;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: 商品库存信息
 * @Date：Created in 2019-10-17
 */
@Slf4j
@Component
public class ProductInfoReceiver {
    private static final String PRODUCT_STOCK_TEMPLATE = "product_stock_%s";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitListener(queuesToDeclare = @Queue("productinfo"))
    @RabbitHandler
    public void receive(String message){
        // 1.message => ProductInfoOutput
        List<ProductInfoOutput> productInfoOutputList = (List<ProductInfoOutput>)GsonUtil.fromJson(message, new TypeToken<List<ProductInfoOutput>>(){}.getType());
        log.info("从队列【{}】中获得消息:{}","productinfo",productInfoOutputList);
        // 2.存储到redis
        for (ProductInfoOutput ptoductInfoOutput: productInfoOutputList) {
            stringRedisTemplate.opsForValue().set(String.format(PRODUCT_STOCK_TEMPLATE,ptoductInfoOutput.getProductId()),String.valueOf(ptoductInfoOutput.getProductStock()));
        }
    }
}
