package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.order.StateOfOrderEntity;
import ir.fassih.workshopautomation.entity.orderstate.OrderStateEntity;
import ir.fassih.workshopautomation.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderManager extends AbstractManager<OrderEntity, Long> {

    @Autowired
    OrderStateManager orderStateManager;

    @Autowired
    public OrderManager(OrderRepository repository) {
        super(repository, OrderEntity.class);
    }

    @Transactional
    public void nextState(Long id) {
        OrderEntity orderEntity = find(id);
        OrderStateEntity currentState = orderEntity.getCurrentState();
        if (currentState != null) {
            OrderStateEntity nextState = orderStateManager.nextOf(currentState);
            StateOfOrderEntity state = new StateOfOrderEntity();
            state.setOrder( orderEntity );
            state.setCreateDate( new Date() );
            state.setState( nextState );
            orderEntity.putToState( state );
        }
    }
}
