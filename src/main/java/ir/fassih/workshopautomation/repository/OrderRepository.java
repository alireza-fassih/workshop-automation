package ir.fassih.workshopautomation.repository;

import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.user.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends AbstractRepository<OrderEntity, Long> {


    long countByCreator(UserEntity user);

    @Modifying
    @Query("update OrderEntity o set o.discount = ?2 where o.id = ?1")
    void setDiscount(Long id, Long discount);
}
