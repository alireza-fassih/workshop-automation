package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.order.OrderGoodsEntity;
import ir.fassih.workshopautomation.entity.order.OrderItemEntity;
import ir.fassih.workshopautomation.entity.user.UserEntity;
import ir.fassih.workshopautomation.manager.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Secured("ADMIN")
@RestController
@RequestMapping("/rest/allOrder")
public class AllOrderService extends AbstractRestService<OrderEntity, Long> {

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
        metadata.put(UserManager.class, "users");
        return metadata;
    }


    @Override
    protected String getEntityName() {
        return "orders";
    }

    @Override
    public List<String> getXslHeaders() {
        return Arrays.asList("شناسه", "سفارش", "قیمت", "تاریخ ثبت", "سفارش دهنده");
    }

    @Override
    protected List<String> convertToRaw(OrderEntity entity) {
        return Arrays.asList(
                entity.getId().toString(),
                entity.getTitle(),
                Optional.ofNullable(entity.getItems()).orElse(new ArrayList<>()).stream()
                        .flatMap(e -> Optional.ofNullable(e.getItems()).orElse(new ArrayList<>()).stream())
                        .map(OrderItemEntity::getPrice)
                        .reduce((o, n) -> o + n).orElse(0L).toString(),
                new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(entity.getCreateDate()),
                Optional.ofNullable(entity.getCreator())
                        .map(UserEntity::getUsername).orElse("")
        );
    }

}
