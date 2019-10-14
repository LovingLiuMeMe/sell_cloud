package cn.lovingliu.product.service.impl;

import cn.lovingliu.product.dataobject.ProductCategory;
import cn.lovingliu.product.repository.ProductCategoryRepository;
import cn.lovingliu.product.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public List<ProductCategory> findByCategoryTypeInIdList(List<Integer> categoryIdList) {
        return productCategoryRepository.findByCategoryTypeIn(categoryIdList);
    }
}
