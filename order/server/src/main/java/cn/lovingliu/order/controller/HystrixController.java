package cn.lovingliu.order.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-20
 */
@RestController
@DefaultProperties(defaultFallback = "defaultFallback")
public class HystrixController {
    @GetMapping("/getProductInfoList")
    public String getProductInfoListNoHystrix(){
        /**
         * 使用传统的 RestTemplate
         */
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("http://127.0.0.1:8000/product/listForOrder", Arrays.asList("test"),String.class);
    }

    @GetMapping("/getProductInfoListHystrix")
    //@HystrixCommand(fallbackMethod = "fallback") // 服务降级

    //@HystrixCommand(commandProperties = { // 触发服务降级的 超时时间
    //     //设置超时时间
    //     @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="3000")
    //})

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"), // 设置熔断
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "1000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60")
    })
    public String getProductInfoList(){
        /**
         * 使用传统的 RestTemplate
         */
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("http://127.0.0.1:8000/product/listForOrder", Arrays.asList("test"),String.class);

        //throw new RuntimeException("异常也可导致服务降级");
    }

    /**
     * @Desc 服务调用时的回调
     * @Author LovingLiu
    */
    private String fallback(){
        return "当前请求人数过多,请稍后再试";
    }

    private String defaultFallback(){
        return "处理所有未单独指定回调的 服务降级 方法";
    }

}
