package cn.lovingliu.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：LovingLiu
 * @Description: 测试配置中心
 * @Date：Created in 2019-10-15
 */
@RestController
@RequestMapping("/env")
@RefreshScope
public class EnvTestController {

    @Value("${env}")
    private String env;

    @GetMapping("/print")
    public String print() {
        return env;
    }

    @Autowired
    private GirlConfig girlConfig;

    @GetMapping("/girl")
    public String girl(){

        return "girl: name="+girlConfig.getName()+" age="+girlConfig.getAge();
    }
}
