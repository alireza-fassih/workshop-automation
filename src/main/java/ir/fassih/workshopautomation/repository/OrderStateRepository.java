package ir.fassih.workshopautomation.repository;

import ir.fassih.workshopautomation.entity.orderstate.OrderStateEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

@Repository
public interface OrderStateRepository extends AbstractRepository<OrderStateEntity, Long> {

    OrderStateEntity findOneByCode(String code);

}
