## 1.开始创建默认的Zuul网关
Zuul原理
zuul的核心是一系列的过滤器，这些过滤器分位四个类型  
"pre"：位置在路由请求到服务调用之前，一般可用于数据校验和限流等  
"route"：这类过滤器将请求路由到微服务，用于构建发送给微服务的请求，并发起微服务请求。  
"post"：这类过滤器在路由到微服务之后请求，可以对返回的数据进行处理或包装  
"error"：其他三类过滤器执行中如果发生错误，会执行该类过滤器。  

构建服务网关  
1.添加依赖
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.0.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>cn.lovingliu</groupId>
    <artifactId>api-gateway</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>api-gateway</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <spring-cloud.version>Hoxton.M3</spring-cloud.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <repositories>
        <repository>
            <id>spring-milestones</id>
            <name>Spring Milestones</name>
            <url>https://repo.spring.io/milestone</url>
        </repository>
    </repositories>

</project>
```
2.创建应用主类，并使用@EnableZuulProxy注解开启Zuul的功能。  
```java
@SpringBootApplication
@EnableZuulProxy
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
```
3.创建配置文件application.yaml，并加入服务名、端口号、eureka注册中心的地址
```xml
spring:
  application:
    name: api-gateaway
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
到这里，一个基于Spring Cloud Zuul服务网关就已经构建完毕。启动该应用，一个默认的服务网关就构建完毕了。  
由于Spring Cloud Zuul在整合了Eureka之后，具备默认的服务路由功能，即：当我们这里构建的api-gateway应用启动并注册到eureka之后，服务网关会发现上面我们启动的两个服务eureka-client和eureka-consumer，这时候Zuul就会创建两个路由规则。  
每个路由规则都包含两部分，一部分是外部请求的匹配规则，另一部分是路由的服务ID。针对当前示例的情况，Zuul会创建下面的两个路由规则：

1.转发到eureka-client服务的请求规则为：`/eureka-client/**`
2.转发到eureka-consumer服务的请求规则为：`/eureka-consumer/**`

```
注意: 在访问http://localhost:8002/config/product-dev.yml并不成功
添加如下代码才成功
ribbon:
  ReadTimeout: 60000
  ConnectTimeout: 60000
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 60000
```
### 2.自定义路由规则
```xml
# 自定义路由
zuul:
  routes:
    myProduct: # 自定义路由名称 http://localhost:8002/myProduct/product/list
      path: /myProduct/** # 要路由的地址
      serviceId: product # 要路由的服务
```
### 3.路由可视化
```xml
# 路由可视化（实际上就是开发端口） http://localhost:8002/actuator/routes
management:
  endpoints:
    web:
      exposure:
        include: "*"
```
返回值
```json
{
    "/myProduct/**": "product",
    "/config/**": "config",
    "/order/**": "order",
    "/product/**": "product"
}
```

### 4.使用config配置中心实现动态路由
1.引入依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
```
2.将公告配置文件上传
```xml
# 自定义路由（测试）
zuul:
  routes:
    myProduct: # 自定义路由名称 http://localhost:8002/myProduct/product/list
      path: /myProduct/** # 要路由的地址
      serviceId: product # 要路由的服务
      # 默认cookie是无法传递的 sensitiveHeaders（敏感头）设置了cookie,所以无法传递cookie
      sensitiveHeaders:
    #简洁写法
    product: /simpleMyProduct/** # http://localhost:8002/simpleMyProduct/product/list
  # 禁止某些路由(正则表达式)
  ignored-patterns:
      - /product/product/list
      - /myProduct/product/list
# 路由可视化（实际上就是开发端口） http://localhost:8002/actuator/routes
management:
  endpoints:
    web:
      exposure:
        include: "*"
```
3.修改bootstrap.yml
```xml
spring:
  application:
    name: api-gateway
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
  instance:
    instance-id: ${spring.application.name}:${spring.cloud.client.ip-address}:${spring.application.instance_id:${server.port}}
    prefer-ip-address: true
ribbon:
  ReadTimeout: 60000
  ConnectTimeout: 60000
hystrix:
  command:
    default:
      execution:
        isolation:
          thread:
            timeoutInMilliseconds: 60000
```
4.修改启动类
```xml
package cn.lovingliu.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @ConfigurationProperties("zuul")
    @RefreshScope
    public ZuulProperties zuulProperties() {
        return new ZuulProperties();
    }

}
```
