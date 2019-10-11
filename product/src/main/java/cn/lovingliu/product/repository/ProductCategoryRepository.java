package cn.lovingliu.product.repository;

import cn.lovingliu.product.dataobject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: ProductCategoryRepository
 * @Date：Created in 2019-10-11
 */
@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
