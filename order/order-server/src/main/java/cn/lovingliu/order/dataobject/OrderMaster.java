package cn.lovingliu.order.dataobject;

import cn.lovingliu.order.enums.CommonStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
@Table(name = "order_master")
@Entity
@DynamicUpdate
@Data
public class OrderMaster {
    @Id
    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerOpenid;
    private BigDecimal orderAmount;
    // 订单状态 默认为新下单
    private Integer orderStatus = CommonStatusEnum.NEW_ORDER.getCode();
    // 支付状态 默认为未支付
    private Integer payStatus = CommonStatusEnum.WAIT_PAY.getCode();

    private Date createTime;

    private Date updateTime;
}
