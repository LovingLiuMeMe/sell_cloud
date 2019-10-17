# 统一配置中心
该项目的模版是 eureka的server 同时是cloud-config的server

## 印射规则
/{name}-{profiles}.yml
/{label}/{name}-{profiles}.yml

name:服务名
profiles:环境
label:分支（默认master）

## 高可用
多启动几个实例（如:同一个服务启动不同的端口） 即可实现负载均衡的高可用

## 使用Spring-Cloud-Bus


### Docker RabbitMQ 
```
docker run -d --name myrabbit -p 5672:5672 -p 15672:15672 rabbitmq:3.8.0-management
docker run -d -p 6379:6379 redis:4.0.8
```

1.停止所有的container，这样才能够删除其中的images：

docker stop $(docker ps -a -q)

如果想要删除所有container的话再加一个指令：

docker rm $(docker ps -a -q)

2.查看当前有些什么images

docker images

3.删除images，通过image的id来指定删除谁

docker rmi <image id>

想要删除untagged images，也就是那些id为<None>的image的话可以用

docker rmi $(docker images | grep "^<none>" | awk "{print $3}")

要删除全部image的话

docker rmi $(docker images -q)

docker run rabbitmq:3.8.0-management

### 架构

config 项目的启动类中`@EnableConfigServer`,其实代表的是`Config-Server`
而 需要用到配置的服务 应该注册为 `Config-Client` 

 
1.暴露接口(bus-refresh)
````xml
management:
  # 暴露bus-refresh
  endpoints:
    web:
      exposure:
        include: "*"
````
2.添加暴露接口依赖(坑:未依赖) 
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
**上述完成之后，可以使用postMan工具，在git仓库更新的时候，直接手动的访问`http://2a3789d893.zicp.vip/actuator/bus-refresh`地址,实现配置文件的更新。**
  
### 解决手动刷新的痛点
3.添加WebHooks依赖(坑:未依赖)  
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-monitor</artifactId>
</dependency>
```
4.暴露monitor接口(坑:还以为是bus-refresh)
```xml
上面暴露的是所有的接口 无需再暴露.
```
5.设置webHooks  
注意不是

### github的webHooks
Webhooks是GitHub提供的一个API。Webhooks可以在GitHub仓库(repositories)发生事件(比如提交代码,创建分支，发布版本)时，通知到其他服务器

在配置文件的仓库中设置(Settings)中设置即可。**注意:不再是设置`bus-refresh`接口,而是spring-cloud提供的monitor: `http://2a3789d893.zicp.vip/monitor`**
