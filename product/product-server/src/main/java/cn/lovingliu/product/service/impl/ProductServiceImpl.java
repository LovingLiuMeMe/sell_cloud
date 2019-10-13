package cn.lovingliu.product.service.impl;

import cn.lovingliu.product.common.DecreaseStockInput;
import cn.lovingliu.product.dataobject.ProductInfo;
import cn.lovingliu.product.enums.CommonStatusEnum;
import cn.lovingliu.product.exception.ProductException;
import cn.lovingliu.product.repository.ProductInfoRepository;
import cn.lovingliu.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<ProductInfo> findList(List<String> productIdList) {
        return productInfoRepository.findByProductIdIn(productIdList);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.REPEATABLE_READ)
    public void decreaseStock(List<DecreaseStockInput> decreaseStockInputList) {
        for (DecreaseStockInput decreaseStockInput: decreaseStockInputList) {
            ProductInfo productInfo = productInfoRepository.findById(decreaseStockInput.getProductId()).orElse(null);

            if (productInfo == null){
                throw new ProductException(CommonStatusEnum.PRODUCT_NOT_EXIT);
            }
            Integer resultStock = productInfo.getProductStock() - decreaseStockInput.getProductQuantity();
            /**
             * @Desc 超卖现象
             * 假设此时有两个线程同时进入这里，库存10件，且都买9件，此时就会出现超卖。卖出的数量超过库存
             * @Author LovingLiu
             */
            if(resultStock < 0){
                throw new ProductException(CommonStatusEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(resultStock);
            productInfoRepository.save(productInfo);
        }
    }

    @Override
    public ProductInfo findById(String id) {
        Optional<ProductInfo> optional = productInfoRepository.findById(id);
        if(!optional.isPresent()){
            throw new ProductException(CommonStatusEnum.PRODUCT_NOT_EXIT);
        }
        return optional.get();
    }
}
