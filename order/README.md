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
### 4.spring-cloud-stream-rabbit
应用程序通过 input（相当于 consumer）、output（相当于 producer）来与 Spring Cloud Stream 中 Binder 交互，而 Binder 负责与消息中间件交互；  
因此，我们只需关注如何与 Binder 交互即可，而无需关注与具体消息中间件的交互。
#### 创建生产者
1.添加依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
</dependency>
```
2.定义配置文件
```xml
spring:
  cloud:
    stream:
      binders:
        test:
          type: rabbit
          environment:
            spring:
              rabbitmq:
                addresses: 10.0.20.132
                port: 5672
                username: root
                password: root
                virtual-host: /unicode-pay
      bindings:
        testOutPut:
          destination: testRabbit
          content-type: application/json
          default-binder: test
```
```
现在来解释一下这些配置的含义

binders： 这是一组binder的集合，这里配置了一个名为test的binder，这个binder中是包含了一个rabbit的连接信息
bindings：这是一组binding的集合，这里配置了一个名为testOutPut的binding，这个binding中配置了指向名test的binder下的一个交换机testRabbit。
扩展： 如果我们项目中不仅集成了rabbit还集成了kafka那么就可以新增一个类型为kafka的binder、如果项目中会使用多个交换机那么就使用多个binding，
```
3.创建通道
```java
public interface  MqMessageSource {

    String TEST_OUT_PUT = "testOutPut";

    @Output(TEST_OUT_PUT)
    MessageChannel testOutPut();

}
```
4.发送消息
```java
@EnableBinding(MqMessageSource.class)
public class MqMessageProducer {
    @Autowired
    @Output(MqMessageSource.TEST_OUT_PUT)
    private MessageChannel channel;


    public void sendMsg(String msg) {
        channel.send(MessageBuilder.withPayload(msg).build());
        System.err.println("消息发送成功："+msg);
    }
}
```
这里就是使用上方的通道来发送到指定的交换机了。需要注意的是withPayload方法你可以传入任何类型的对象，但是需要实现序列化接口  
5.创建测试接口    
EnableBinding注解绑定的类默认是被Spring管理的，我们可以在controller中注入它
```java
@Autowired
private MqMessageProducer mqMessageProducer;

@GetMapping(value = "/testMq")
public String testMq(@RequestParam("msg")String msg){
    mqMessageProducer.sendMsg(msg);
    return "发送成功";
}
```
生产者的代码到此已经完成了。
#### 创建消费者
1. 引入依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
</dependency>
```
2.定义配置文件
```xml
spring:
  cloud:
    stream:
      binders:
        test:
          type: rabbit
          environment:
            spring:
              rabbitmq:
                addresses: 10.0.20.132
                port: 5672
                username: root
                password: root
                virtual-host: /unicode-pay
      bindings:
        testInPut:
          group: order
          destination: testRabbit
          content-type: application/json
          default-binder: test
```
这里与生产者唯一不同的地方就是testIntPut了，相信你已经明白了，它是binding的名字，也是通道与交换机绑定的关键  
注意: `group: order` 是的多个消费者 消费同一个queue下的消息,而不是各自维护一个  
```
如:不使用group: order 之前
Queue order-output.anonymous.DTMaZ5F6TJGhRKGbcVRu8Q
    -> exchange: order-output
    -> routing key: #
Queue order-output.anonymous.dGPeoKsGRZC2o1YKC4bEBw
    -> exchange: order-output
    -> routing key: #

添加group: order 之后

    
```
3.创建通道  
```java
public interface  MqMessageSource {

    String TEST_IN_PUT = "testInPut";

    @Input(TEST_IN_PUT)
    SubscribableChannel testInPut();

}
```
4.接受消息
```java
@EnableBinding(MqMessageSource.class)
public class MqMessageConsumer {
    @StreamListener(MqMessageSource.TEST_IN_PUT)
    public void messageInPut(Message<String> message) {
        System.err.println(" 消息接收成功：" + message.getPayload());
    }

}
```
这个时候启动Eureka、消息生产者和消费者，然后调用生产者的接口应该就可以接受到来自mq的消息了。
