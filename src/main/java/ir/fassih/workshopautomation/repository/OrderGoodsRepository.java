package ir.fassih.workshopautomation.repository;


import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.order.OrderGoodsEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderGoodsRepository  extends  AbstractRepository<OrderGoodsEntity, Long>{

    void deleteByOrder(OrderEntity orderEntity);

}
