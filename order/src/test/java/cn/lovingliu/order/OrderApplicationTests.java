package cn.lovingliu.order;

import cn.lovingliu.order.client.ProductClient;
import cn.lovingliu.order.dto.CartDTO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderApplicationTests {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplateConfig;

    @Test
    public void contextLoads() {
        // 1.直接使用RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject("http://localhost:8080/product/list", String.class);
        log.error("商品服务的远程调用,方式1 {}",json);

        // 2.使用springCloud提供的 LoadBalancerClient,通过 应用名 获取url
        ServiceInstance serviceInstance = loadBalancerClient.choose("PRODUCT");
        String url = String.format("http://%s:%s",serviceInstance.getHost(),serviceInstance.getPort());
        json = restTemplate.getForObject(url,String.class);
        log.error("商品服务的远程调用,方式2 {}",json);

        // 3.使用@LoadBalanced 直接使用应用名称
        json = restTemplateConfig.getForObject("http://PRODUCT/product/list",String.class);
        log.error("商品服务的远程调用,方式2 {}",json);
    }


    @Autowired
    private ProductClient productClient;

    @Test
    public void testFeign(){
        String resultJson = productClient.productList();
        log.info("resultJson = {}",resultJson);

        String resultMsg = productClient.decreaseStock(Lists.newArrayList(
                new CartDTO("1570451410455390493",12),
                new CartDTO("test",12)
        ));
        log.info(resultMsg);
    }

}
