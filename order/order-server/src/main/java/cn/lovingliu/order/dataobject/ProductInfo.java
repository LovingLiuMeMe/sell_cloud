package cn.lovingliu.order.dataobject;

import cn.lovingliu.order.enums.CommonStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author：LovingLiu
 * @Description: ProductInfo的实体类
 * @Date：Created in 2019-10-11
 */
@Entity
@DynamicUpdate
@Data
public class ProductInfo {
    @Id
    private String productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer productStock;
    private String productDescription;
    private String productIcon;

    private Integer productStatus = CommonStatusEnum.UP.getCode();
    private Integer categoryType;

    private Date createTime;
    private Date updateTime;
}
