package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.manager.AbstractManager;
import ir.fassih.workshopautomation.manager.GoodsManager;
import ir.fassih.workshopautomation.manager.OrderManager;
import ir.fassih.workshopautomation.manager.OrderStateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/rest/allOrder")
public class AllOrderService extends  AbstractRestService<OrderEntity, Long> {

    private OrderManager getMyManager() {
        return (OrderManager) manager;
    }

    @Autowired
    public AllOrderService(OrderManager manager) {
        super(manager);
    }

    @PostMapping("/{id}/nextState")
    public void nextState(@PathVariable("id") Long id) {
        getMyManager().nextState(id);
    }

    @Override
    protected Map<Class<? extends AbstractManager>, String> getOptionsMetadata() {
        Map<Class<? extends AbstractManager>, String> metadata = super.getOptionsMetadata();
        metadata.put(GoodsManager.class, "products");
        metadata.put(OrderStateManager.class, "states");
        return metadata;
    }


}
