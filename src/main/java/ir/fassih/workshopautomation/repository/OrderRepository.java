package ir.fassih.workshopautomation.repository;

import ir.fassih.workshopautomation.entity.order.OrderEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends AbstractRepository<OrderEntity, Long> {
}
