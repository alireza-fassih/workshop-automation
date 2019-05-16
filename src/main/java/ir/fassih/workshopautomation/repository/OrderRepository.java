package ir.fassih.workshopautomation.repository;

import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.user.UserEntity;
import ir.fassih.workshopautomation.repository.report.CountByTimeModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends AbstractRepository<OrderEntity, Long> {


    long countByCreator(UserEntity user);

    @Modifying
    @Query("update OrderEntity o set o.discount = ?2 where o.id = ?1")
    void setDiscount(Long id, Long discount);


    @Query(
        "SELECT YEAR(o.createDate) AS y, MONTH(o.createDate) AS m, DAY(o.createDate) AS d, SUM(o.unit) AS count " +
            "FROM OrderEntity o " +
            "WHERE o.createDate > ?1 AND o.createDate < ?2 " +
            "GROUP BY YEAR(o.createDate), MONTH(o.createDate), DAY(o.createDate)" )
    List<CountByTimeModel<Double>> reportUnitsByTime(Date start, Date end);



}
