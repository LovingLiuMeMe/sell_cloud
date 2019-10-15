package cn.lovingliu.product.repository;

import cn.lovingliu.product.dataobject.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: ProductInfoRepository
 * @Date：Created in 2019-10-11
 */
@Repository
public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {
    Page<ProductInfo> findByProductStatus(Integer productStatus, Pageable pageable);

    List<ProductInfo> findByProductIdIn(List<String> productIdList);
}
