## 模块华改造
1.为什么要进行改造（目前项目存在的问题）
```java
首先,此时 已经实现了 订单服务调用商品服务的接口,实现了下单,但是存在以下问题
1.相关类 在两个服务中定义了两次
在减库存的接口中 使用到CartDTO,在dto中定义。
// product
public String decreaseStock(@RequestBody List<CartDTO> cartDTOList){
    productService.decreaseStock(cartDTOList);
    return "success";
}
// order
在创建订单的过程中也有使用。也在dto中定义。

2.属于product服务的接口,却在order服务中定义  
在多人写作开发不同服务中是非常不有好的。应该是自己的接口 由自己来暴露
// order
@FeignClient(name = "PRODUCT")
public interface ProductClient {
    @GetMapping("product/list")
    String productList();

    @PostMapping("product/listForOrder")
    List<ProductInfo> listForOrder(@RequestBody List<String> productIdList);

    @PostMapping("product/decreaseStock")
    String decreaseStock(@RequestBody List<CartDTO> cartDTOList);

    @GetMapping("product/info/{id}")
    ProductInfo findById(@PathVariable("id") String id);
}


3.直接使用数据库对象 作为返回对象 非常的不安全
如:List<ProductInfo> 
```

### 如何改造
1.更改打包方式
```xml
<!-- 原来是jar -->
<packaging>pom</packaging>
```
2.自己维护本服务暴露的接口（保证服务的完整性）
创建ProductClient(原来是在order服务上,维护性非常差)
```java
package cn.lovingliu.product.client;

import cn.lovingliu.product.common.DecreaseStockInput;
import cn.lovingliu.product.common.ProductInfoOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: 自己暴露出去的 接口由自己管理
 * @Date：Created in 2019-10-13
 */
@FeignClient(name = "PRODUCT")
public interface ProductClient {
    @GetMapping("product/list")
    String productList();

    @PostMapping("product/listForOrder")
    List<ProductInfoOutput> listForOrder(@RequestBody List<String> productIdList);

    @PostMapping("product/decreaseStock")
    String decreaseStock(@RequestBody List<DecreaseStockInput> cartDTOList);

    @GetMapping("product/info/{id}")
    ProductInfoOutput findById(@PathVariable("id") String id);
}
```
2.自己维护自己的入参类（保证服务的完整性）

### jar包打包
mvn -Dmaven.test.skip=true -U clean install 