package cn.lovingliu.order.client;

import cn.lovingliu.order.dataobject.ProductInfo;
import cn.lovingliu.order.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: 使用Spring Cloud Feign  配置要调用的接口列表
 * PRODUCT: 为应用的名称
 * @Date：Created in 2019-10-11
 */
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
