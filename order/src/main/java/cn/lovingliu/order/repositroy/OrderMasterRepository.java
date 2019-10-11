package cn.lovingliu.order.repositroy;

import cn.lovingliu.order.dataobject.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-10-11
 */
@Repository
public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {

}
