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
