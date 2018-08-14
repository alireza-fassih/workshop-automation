package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.orderstate.OrderStateEntity;
import ir.fassih.workshopautomation.manager.AbstractManager;
import ir.fassih.workshopautomation.manager.OrderStateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/rest/orderState")
public class OrderStateService extends AbstractRestService<OrderStateEntity, Long> {

    @Autowired
    public OrderStateService(OrderStateManager manager) {
        super(manager);
    }


    @Override
    protected Map<String, Object> optionsInternal() {
        Map<String, Object> map = super.optionsInternal();
        map.put("states", manager.loadAll());
        return map;
    }
}
