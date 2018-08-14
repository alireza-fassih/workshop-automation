package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.orderstate.OrderStateEntity;
import ir.fassih.workshopautomation.repository.OrderStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderStateManager extends AbstractManager<OrderStateEntity, Long> {

    @Autowired
    public OrderStateManager(OrderStateRepository repository) {
        super(repository);
    }

}
