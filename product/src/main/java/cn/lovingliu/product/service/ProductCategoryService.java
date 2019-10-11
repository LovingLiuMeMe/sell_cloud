package cn.lovingliu.product.service;

import cn.lovingliu.product.dataobject.ProductCategory;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: 商品分类的service
 * @Date：Created in 2019-10-11
 */
public interface ProductCategoryService {
    List<ProductCategory> findByCategoryTypeInIdList(List<Integer> categoryIdList);
}
