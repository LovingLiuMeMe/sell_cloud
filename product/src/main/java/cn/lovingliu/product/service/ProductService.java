package cn.lovingliu.product.service;

import cn.lovingliu.product.dataobject.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
public interface ProductService {
    Page<ProductInfo> findUpAll(Integer productStatus, Pageable pageable);
}
