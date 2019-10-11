package cn.lovingliu.order.service.impl;

import cn.lovingliu.order.dataobject.OrderDetail;
import cn.lovingliu.order.dataobject.OrderMaster;
import cn.lovingliu.order.dto.CartDTO;
import cn.lovingliu.order.dto.OrderDTO;
import cn.lovingliu.order.enums.CommonStatusEnum;
import cn.lovingliu.order.exception.OrderException;
import cn.lovingliu.order.repositroy.OrderDetailRepository;
import cn.lovingliu.order.repositroy.OrderMasterRepository;
import cn.lovingliu.order.service.OrderService;
import cn.lovingliu.order.util.BigDecimalUtil;
import cn.lovingliu.order.util.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public OrderDTO createrOrder(OrderDTO orderDTO) {
        BigDecimal orderAllAmount = new BigDecimal("0");

        String orderId = KeyUtil.getUniqueKey();

        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            //todo 1.查询商品详情(调用商品的服务)

            Map<String,Object> productInfo = new HashMap<>();

            if (productInfo == null) {
                throw new OrderException(CommonStatusEnum.ERROR);
            }

            //todo 2.计算该订单的所有总价
            orderAllAmount = BigDecimalUtil.add(
                    orderAllAmount.doubleValue(),
                    BigDecimalUtil.mul(
                            productInfo.getProductPrice().doubleValue(),
                            orderDetail.getProductQuantity().doubleValue()
                    ).doubleValue()
            );

            // 3.订单详情入库
            BeanUtils.copyProperties(productInfo, orderDetail);

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
        List<CartDTO> cartDTOList  = orderDTO.getOrderDetailList().stream().map(
                e -> new CartDTO(e.getProductId(),e.getProductQuantity())

        ).collect(Collectors.toList());

        productService.decreaseStock(cartDTOList);


        return orderDTO;
    }
}
