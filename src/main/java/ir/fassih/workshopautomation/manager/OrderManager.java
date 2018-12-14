package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.core.LogicallyDeletable;
import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.order.StateOfOrderEntity;
import ir.fassih.workshopautomation.entity.orderstate.OrderStateEntity;
import ir.fassih.workshopautomation.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderManager extends AbstractManager<OrderEntity, Long> {

    @Autowired
    OrderStateManager orderStateManager;

    @Autowired
    UserManager userManager;


    @Autowired
    public OrderManager(OrderRepository repository) {
        super(repository, OrderEntity.class);
    }


    OrderRepository getMyRepo() {
        return (OrderRepository) repository;
    }


    @Transactional
    public void nextState(Long id) {
        OrderEntity orderEntity = find(id);
        OrderStateEntity currentState = orderEntity.getCurrentState();
        if (currentState != null) {
            putToState(orderStateManager.nextOf(currentState), orderEntity);
        }
    }


    private void putToState(OrderStateEntity state, OrderEntity entity) {
        if( state != null && entity != null ) {
            StateOfOrderEntity orderState = new StateOfOrderEntity();
            orderState.setOrder( entity );
            orderState.setCreateDate( new Date() );
            orderState.setState( state );
            entity.putToState( orderState );
            repository.save( entity );
        }
    }


    @Transactional
    public void delete(Long id) {
        OrderEntity entity = repository.findOne(id);
        if( entity.isEditable() && userManager.loadCurrentUser().getId().equals(entity.getCreator().getId())) {
            repository.delete(entity);
        }
    }


    @Transactional(readOnly = true)
    public long loadCountOfUserOrder() {
        return getMyRepo().countByCreator(userManager.loadCurrentUser());
    }



}
