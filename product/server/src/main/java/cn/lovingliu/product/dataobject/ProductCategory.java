package cn.lovingliu.product.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author：LovingLiu
 * @Description: @GeneratedValue注解存在的意义主要就是为一个实体生成一个唯一标识的主键
 * (JPA要求每一个实体Entity,必须有且只有一个主键) strategy:是指主键生成策略
 * @Date：Created in 2019-10-11
 */
@Table(name = "product_category")
@Entity
@DynamicUpdate
@Data
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;
    private String categoryName;
    private Integer categoryType;

    private Date createTime;
    private Date updateTime;
}
