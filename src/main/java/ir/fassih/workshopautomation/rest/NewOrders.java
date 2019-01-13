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

@Secured("VERIFIER")
@RestController
@RequestMapping("/rest/newOrders")
public class NewOrders  extends AbstractRestService<OrderEntity, Long> {

    @Autowired
    private LocaleUtil localeUtil;

    @Data
    public static class VerifyModel {
        private Long id;
        private String description;
    }

    @Autowired
    public NewOrders(OrderManager manager) {
        super(manager);
    }


    private OrderManager getMyManager() {
        return (OrderManager) manager;
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


    @PostMapping("/accept")
    public void accept(@RequestBody VerifyModel model) {
        getMyManager().accept(model.getId(), model.getDescription());
    }


    @PostMapping("/reject")
    public void reject(@RequestBody VerifyModel model) {
        getMyManager().reject(model.getId(), model.getDescription());
    }


    @Override
    protected String getEntityName() {
        return "orders";
    }

    @Override
    public List<String> getXslHeaders() {
        return Arrays.asList("شناسه", "سفارش", "قیمت", "تاریخ ثبت", "سفارش دهنده", localeUtil.getString("order.details"));
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
