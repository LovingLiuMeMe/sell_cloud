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