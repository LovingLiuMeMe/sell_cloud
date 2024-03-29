package cn.lovingliu.order.service.impl;

import cn.lovingliu.order.dataobject.OrderDetail;
import cn.lovingliu.order.dataobject.OrderMaster;
import cn.lovingliu.order.dto.OrderDTO;
import cn.lovingliu.order.enums.CommonStatusEnum;
import cn.lovingliu.order.exception.OrderException;
import cn.lovingliu.order.repositroy.OrderDetailRepository;
import cn.lovingliu.order.repositroy.OrderMasterRepository;
import cn.lovingliu.order.service.OrderService;
import cn.lovingliu.order.util.BigDecimalUtil;
import cn.lovingliu.order.util.KeyUtil;
import cn.lovingliu.product.client.ProductClient;
import cn.lovingliu.product.common.DecreaseStockInput;
import cn.lovingliu.product.common.ProductInfoOutput;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private ProductClient productClient;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public OrderDTO createrOrder(OrderDTO orderDTO) {
        BigDecimal orderAllAmount = new BigDecimal("0");

        String orderId = KeyUtil.getUniqueKey();

        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            // 1.查询商品详情(调用商品的服务)

            ProductInfoOutput productInfoOutput = productClient.findById(orderDetail.getProductId());

            if (productInfoOutput == null) {
                throw new OrderException(CommonStatusEnum.ERROR);
            }

            // 2.计算该订单的所有总价
            orderAllAmount = BigDecimalUtil.add(
                    orderAllAmount.doubleValue(),
                    BigDecimalUtil.mul(
                            productInfoOutput.getProductPrice().doubleValue(),
                            orderDetail.getProductQuantity().doubleValue()
                    ).doubleValue()
            );

            // 3.订单详情入库
            BeanUtils.copyProperties(productInfoOutput, orderDetail);

            orderDetail.setDetailId(KeyUtil.getUniqueKey());
            orderDetail.setOrderId(orderId);
            orderDetailRepository.save(orderDetail);
        }

        // 4.订单入库
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);

        orderMaster.setOrderStatus(CommonStatusEnum.NEW_ORDER.getCode());
        orderMaster.setPayStatus(CommonStatusEnum.WAIT_PAY.getCode());
        orderMaster.setOrderAmount(orderAllAmount);
        orderMasterRepository.save(orderMaster);

        // 5.扣库存 (就算多个商品也只会调用一次)
        // lambda表达式
        List<DecreaseStockInput> decreaseStockInputList = orderDTO.getOrderDetailList().stream().map(
                e -> new DecreaseStockInput(e.getProductId(), e.getProductQuantity())

        ).collect(Collectors.toList());

        productClient.decreaseStock(decreaseStockInputList);


        return orderDTO;
    }
    /**
     * @Desc 完结订单 只能卖家来操作
     * @Author LovingLiu
    */
    @Transactional
    public OrderDTO finish(String orderId){
        // 1.查询订单
        Optional<OrderMaster> orderMasterOptional = orderMasterRepository.findById(orderId);
        if(!orderMasterOptional.isPresent()){
            throw new OrderException(CommonStatusEnum.ORDER_NOT_EXIT);
        }
        OrderMaster orderMaster = orderMasterOptional.get();
        // 2.判断订单状态
        if(orderMaster.getOrderStatus() != CommonStatusEnum.NEW_ORDER.getCode()){
            throw new OrderException(CommonStatusEnum.ORDER_STATUS_ERROR);
        }
        // 3.修改订单状态
        orderMaster.setOrderStatus(CommonStatusEnum.ORDER_FINISGHED.getCode());
        // 4.更新数据库
        OrderMaster result = orderMasterRepository.save(orderMaster);
        // 5.构造OrderDTO对象
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw new OrderException(CommonStatusEnum.ORDER_NOT_DETAIL_EXIT);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(result,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }
}
