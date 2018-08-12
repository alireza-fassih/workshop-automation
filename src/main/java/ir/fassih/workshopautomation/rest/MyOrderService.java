package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.core.datamanagment.model.SearchModel;
import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.manager.OrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping( "/rest/myOrder" )
public class MyOrderService extends AbstractRestService<OrderEntity, Long> {

    @Autowired
    public MyOrderService(OrderManager manager) {
        super(manager);
    }

    @Override
    public Page<OrderEntity> search(Map<String, String> params) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SearchModel searchModel = createSearchModel(params);
        searchModel.getFilters().put( "EQ:creator.username", authentication.getName() );
        return manager.search(searchModel);
    }

    @Override
    public void save(OrderEntity entity) {
        throw new UnsupportedOperationException();
    }
}
