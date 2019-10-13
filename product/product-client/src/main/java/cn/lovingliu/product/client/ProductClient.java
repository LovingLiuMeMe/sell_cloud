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
