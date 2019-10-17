package cn.lovingliu.product.service.impl;

import cn.lovingliu.product.common.DecreaseStockInput;
import cn.lovingliu.product.common.ProductInfoOutput;
import cn.lovingliu.product.dataobject.ProductInfo;
import cn.lovingliu.product.enums.CommonStatusEnum;
import cn.lovingliu.product.exception.ProductException;
import cn.lovingliu.product.repository.ProductInfoRepository;
import cn.lovingliu.product.service.ProductService;
import cn.lovingliu.product.util.GsonUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public Page<ProductInfo> findUpAll(Integer productStatus, Pageable pageable) {
        Page page = productInfoRepository.findByProductStatus(CommonStatusEnum.UP.getCode(), pageable);
        return page;
    }

    @Override
    public List<ProductInfo> findList(List<String> productIdList) {
        return productInfoRepository.findByProductIdIn(productIdList);
    }

    @Override
    public void decreaseStock(List<DecreaseStockInput> decreaseStockInputList) {
        List<ProductInfo> productInfoList = decreaseStockProcess(decreaseStockInputList);
        // 发送MQ消息
        /**
         * 这里 会有问题,加入有两件商品A(stock:20)B(stock:0)
         * A 扣库存正常 -> 数据库预扣库存（事务管理）-> MQ消息发布
         * B 扣库存异常:PRODUCT_STOCK_ERROR -> 事务回滚 -> A商品 不回扣库存
         *
         * 但是 A商品扣库存的MQ消息却已经发出
        */
        List<ProductInfoOutput> productInfoOutputList = productInfoList.stream().map(e -> {
            ProductInfoOutput productInfoOutput = new ProductInfoOutput();
            BeanUtils.copyProperties(e,productInfoOutput);
            return productInfoOutput;
        }).collect(Collectors.toList());

        //amqpTemplate.convertAndSend("order-output","order.ABC", GsonUtil.toJson(productInfo));
        amqpTemplate.convertAndSend("productinfo",GsonUtil.toJson(productInfoOutputList)); // routing-key == queue 也能发送 exchange 是默认的
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public List<ProductInfo> decreaseStockProcess(List<DecreaseStockInput> decreaseStockInputList) {
        List<ProductInfo> productInfoList = new ArrayList<>();

        for (DecreaseStockInput decreaseStockInput : decreaseStockInputList) {
            ProductInfo productInfo = productInfoRepository.findById(decreaseStockInput.getProductId()).orElse(null);

            if (productInfo == null) {
                throw new ProductException(CommonStatusEnum.PRODUCT_NOT_EXIT);
            }
            Integer resultStock = productInfo.getProductStock() - decreaseStockInput.getProductQuantity();
            /**
             * @Desc 超卖现象
             * 假设此时有两个线程同时进入这里，库存10件，且都买9件，此时就会出现超卖。卖出的数量超过库存
             * @Author LovingLiu
             */
            if (resultStock < 0) {
                throw new ProductException(CommonStatusEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(resultStock);
            productInfoRepository.save(productInfo);

            // 发送MQ消息
            /**
             * 这里 会有问题,加入有两件商品A(stock:20)B(stock:0)
             * A 扣库存正常 -> 数据库预扣库存（事务管理）-> MQ消息发布
             * B 扣库存异常:PRODUCT_STOCK_ERROR -> 事务回滚 -> A商品 不回扣库存
             *
             * 但是 A商品扣库存的MQ消息却已经发出
             */
            productInfoList.add(productInfo);
        }
        return productInfoList;
    }

    @Override
    public ProductInfo findById(String id) {
        Optional<ProductInfo> optional = productInfoRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ProductException(CommonStatusEnum.PRODUCT_NOT_EXIT);
        }
        return optional.get();
    }
}
