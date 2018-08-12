package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.manager.OrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/rest/order" )
public class OrderService extends AbstractRestService<OrderEntity, Long> {

    @Autowired
    public OrderService(OrderManager manager) {
        super(manager);
    }

}
