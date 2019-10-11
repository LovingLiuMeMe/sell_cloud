package cn.lovingliu.product.service.impl;

import cn.lovingliu.product.dataobject.ProductInfo;
import cn.lovingliu.product.enums.CommonStatusEnum;
import cn.lovingliu.product.repository.ProductInfoRepository;
import cn.lovingliu.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public Page<ProductInfo> findUpAll(Integer productStatus, Pageable pageable) {
        Page page = productInfoRepository.findByProductStatus(CommonStatusEnum.UP.getCode(),pageable);
        return page;
    }
}
