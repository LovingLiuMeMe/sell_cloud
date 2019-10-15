package cn.lovingliu.order.repositroy;

import cn.lovingliu.order.dataobject.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
}
