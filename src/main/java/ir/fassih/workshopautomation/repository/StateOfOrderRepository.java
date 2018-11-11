package ir.fassih.workshopautomation.repository;

import ir.fassih.workshopautomation.entity.order.StateOfOrderEntity;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StateOfOrderRepository  extends  AbstractRepository<StateOfOrderEntity, Long> {



}
