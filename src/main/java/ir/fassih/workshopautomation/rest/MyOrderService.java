package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.core.datamanagment.model.SearchModel;
import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.order.OrderGoodsEntity;
import ir.fassih.workshopautomation.entity.order.OrderItemEntity;
import ir.fassih.workshopautomation.entity.user.UserEntity;
import ir.fassih.workshopautomation.manager.AbstractManager;
import ir.fassih.workshopautomation.manager.GoodsManager;
import ir.fassih.workshopautomation.manager.OrderManager;
import ir.fassih.workshopautomation.manager.OrderStateManager;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Secured("USER")
@RestController
@RequestMapping("/rest/myOrder")
public class MyOrderService extends AbstractRestService<OrderEntity, Long> {


    @Autowired
    public MyOrderService(OrderManager manager) {
        super(manager);
    }

    @Override
    protected String getEntityName() {
        return "orders";
    }

    @Override
    public List<String> getXslHeaders() {
        return Arrays.asList("شناسه" , "سفارش", "قیمت", "تاریخ ثبت");
    }

    @Override
    public Page<OrderEntity> search(@RequestParam Map<String, String> params) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SearchModel searchModel = createSearchModel(params);
        searchModel.getFilters().put("EQ:creator.username", authentication.getName());
        return manager.search(searchModel);
    }

    @GetMapping("/reportExcelReport")
    public void makeExcelReport(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        SearchModel searchModel = createSearchModel(params);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        searchModel.getFilters().put("EQ:creator.username", authentication.getName());
        List<List<String>> entities = manager.findAll(searchModel).stream().map(this::convertToRaw)
                .collect(Collectors.toList());
        generateExcelFile(getEntityName(), entities, response);
    }



    @Override
    protected List<String> convertToRaw(OrderEntity entity) {
        return Arrays.asList(
                entity.getId().toString(),
                entity.getTitle(),
                Long.toString(entity.getTotlaPrice()),
                new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(entity.getCreateDate())
        );
    }


    @Override
    protected Map<Class<? extends AbstractManager>, String> getOptionsMetadata() {
        Map<Class<? extends AbstractManager>, String> metadata = super.getOptionsMetadata();
        metadata.put(GoodsManager.class, "products");
        metadata.put(OrderStateManager.class, "states");
        return metadata;
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
