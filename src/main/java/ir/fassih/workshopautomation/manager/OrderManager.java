package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderManager extends AbstractManager<OrderEntity, Long> {

    @Autowired
    public OrderManager(OrderRepository repository) {
        super(repository);
    }


}
