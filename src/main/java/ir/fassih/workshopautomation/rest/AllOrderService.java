package ir.fassih.workshopautomation.rest;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.manager.AbstractManager;
import ir.fassih.workshopautomation.manager.GoodsManager;
import ir.fassih.workshopautomation.manager.OrderManager;
import ir.fassih.workshopautomation.manager.OrderStateManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/allOrder")
public class AllOrderService extends  AbstractRestService<OrderEntity, Long> {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private GoodsManager goodsManager;

    @Autowired
    private OrderStateManager orderStateManager;


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
    protected Map<String, Object> optionsInternal() {
        Map<String, Object> optionsInternal = super.optionsInternal();
        optionsInternal.put("products", goodsManager.loadNotDeletes().stream().map(p -> mapper.map(p, MyOrderService.ProductDto.class))
                .collect(Collectors.toList()));
        optionsInternal.put("states", orderStateManager.loadAll());
        return optionsInternal;
    }


}
