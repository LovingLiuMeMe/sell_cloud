package cn.lovingliu.product.service;

import cn.lovingliu.product.common.DecreaseStockInput;
import cn.lovingliu.product.dataobject.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
public interface ProductService {
    Page<ProductInfo> findUpAll(Integer productStatus, Pageable pageable);
    /**
     * @Desc 订单服务调用
     * @Author LovingLiu
    */
    List<ProductInfo> findList(List<String> productIdList);
    /**
     * @Desc 扣库存
     * @Author LovingLiu
    */
    void decreaseStock(List<DecreaseStockInput> decreaseStockInputList);

    ProductInfo findById(String id);
}
