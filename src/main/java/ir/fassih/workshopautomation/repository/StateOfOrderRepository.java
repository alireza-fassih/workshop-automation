package ir.fassih.workshopautomation.repository;

import ir.fassih.workshopautomation.entity.order.StateOfOrderEntity;
import ir.fassih.workshopautomation.repository.report.CountByTimeModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
public interface StateOfOrderRepository  extends  AbstractRepository<StateOfOrderEntity, Long> {



    @Query("SELECT YEAR(so.createDate) AS y, MONTH(so.createDate) AS m, DAY(so.createDate) AS d, COUNT(so) AS count " +
                "FROM StateOfOrderEntity so " +
                "WHERE so.state.id = ?1 AND so.order.creator.id = ?2 AND so.createDate > ?3 AND so.createDate < ?4 " +
                "GROUP BY YEAR(so.createDate), MONTH(so.createDate), DAY(so.createDate)")
    List<CountByTimeModel<Long>> reportByStateAndUser(long stateId, long userId, Date startDate, Date endDate);


    @Query("SELECT YEAR(so.createDate) AS y, MONTH(so.createDate) AS m, DAY(so.createDate) AS d, COUNT(so) AS count " +
            "FROM StateOfOrderEntity so " +
            "WHERE so.state.id = ?1 AND so.createDate > ?2 AND so.createDate < ?3 " +
            "GROUP BY YEAR(so.createDate), MONTH(so.createDate), DAY(so.createDate)")
    List<CountByTimeModel<Long>> reportByState(long stateId, Date startDate, Date endDate);


}
