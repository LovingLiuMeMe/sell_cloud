### 服务的调用两种方式
1.RestTemplate  
```java
@Component
public class RestTemplateConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
```
```java
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

}
```
2.Feign  
Spring Cloud Feign是一套基于Netflix Feign实现的声明式服务调用客户端。


#### 2.Spring Cloud Ribbon
spring Cloud Ribbon是基于Netflix Ribbon实现的一套客户端负载均衡的工具。它是一个基于HTTP和TCP的客户端负载均衡器。它可以通过在客户端中配置ribbonServerList来设置服务端列表去轮询访问以达到均衡负载的作用。
```
who is 负载均衡
若 product 在eureka中注册了 两个,当order中调用谁呢?
选择的过程 就是一个负载均衡的过程
```
Ribbon 实现负载均衡 主要又3点:  
1.服务发现  
2.服务选择规则  
3.服务监听  

主要组件: ServerList(获得服务列表)->ServerListFilter(过滤一部分服务)->IRule(选择服务)  
ServerList    
IRule  
ServerListFilter  

#### 3.配置文件统一管理
1.添加依赖  
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-client</artifactId>
</dependency>
```
2.修改application.yml文件内容 
```xml
spring:
  application:
    name: order
  # 统一配置文件管理 并不需要添加其他注解
  cloud:
    config:
      discovery:
        enabled: true
        service-id: CONFIG #应用名称
      profile: dev
# eureka 必须单独提取出来（启动在8761默认端口就不用）
eureka:
  client:
    service-url:
      defaultZone: http://localhost:4321/eureka
```
3.修改`application.yml`文件名称为`bootstrap.yml`
```
在修改完application.yml 文件内容时候,无法启动
愿意: 从config-server上拿取数据库配置文件还未完成,项目就启动。启动时候 发现数据库配置没有 报错。
所以必须要在项目启动之前将 配置文件获取成功
```
4.为什么要将eureka的配置提取出来呢
```
无论怎么说,为服务必须在正确的注册中心 注册之后,才能访问呀。
config 在注册中心上暴露接口 以供获得配置信息
所以你至少要访问 注册中心吧。（注意:当没有配置/配置的地址 找不到时,默认访问的端口是8761）
```
5.配置信息更新广播  
要成为一个`Config-Client`  
1.添加依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
```
2.在使用到配置的地方添加`@RefreshScope`
```java
package cn.lovingliu.order.controller;

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
}
```

