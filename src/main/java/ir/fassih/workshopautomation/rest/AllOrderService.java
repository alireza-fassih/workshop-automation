package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.configuration.locale.LocaleUtil;
import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.user.UserEntity;
import ir.fassih.workshopautomation.manager.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Secured("ADMIN")
@RestController
@RequestMapping("/rest/allOrder")
public class AllOrderService extends AbstractRestService<OrderEntity, Long> {

    @Data
    private static class DiscountModel {
        private Long id;
        private Long discount;
    }

    @Autowired
    private LocaleUtil localeUtil;

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

    @PostMapping("/discount")
    public void discount(@RequestBody DiscountModel model) {
        getMyManager().discount(model.getId(), model.getDiscount());
    }

    @Override
    protected String getEntityName() {
        return "orders";
    }

    @Override
    public List<String> getXslHeaders() {
        return Arrays.asList("شناسه", "سفارش", "قیمت", "تاریخ ثبت","سفارش دهنده", localeUtil.getString("order.details"));
    }

    @Override
    protected List<String> convertToRaw(OrderEntity entity) {
        return Arrays.asList(
                entity.getId().toString(),
                entity.getTitle(),
                Long.toString(entity.getTotlaPrice()),
                new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(entity.getCreateDate()),
                Optional.ofNullable(entity.getCreator())
                        .map(UserEntity::getUsername).orElse(""),
                entity.createDetails()
        );
    }

    @Override
    public void update(Long id, OrderEntity entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(OrderEntity entity) {
        throw new UnsupportedOperationException();
    }


}
