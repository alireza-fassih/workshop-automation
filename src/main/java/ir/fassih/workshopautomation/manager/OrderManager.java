package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.core.LogicallyDeletable;
import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.order.StateOfOrderEntity;
import ir.fassih.workshopautomation.entity.orderstate.OrderStateEntity;
import ir.fassih.workshopautomation.entity.user.UserEntity;
import ir.fassih.workshopautomation.repository.OrderRepository;
import ir.fassih.workshopautomation.security.PortalRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderManager extends AbstractManager<OrderEntity, Long> {

    @Autowired
    OrderStateManager orderStateManager;

    @Autowired
    UserManager userManager;

    @Autowired
    private JdbcTemplate template;

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
    public int recalculateAllUnits() {
        String sql = "UPDATE DASH_ORDER SET unit=( " +
                    "select DASH_ORDER_GOODS.ITEM_COUNT * sum(DASH_GOODS.width) as unit from " +
                        "DASH_ORDER_GOODS INNER JOIN DASH_GOODS ON DASH_ORDER_GOODS.GOODS = DASH_GOODS.ID " +
                        "WHERE DASH_ORDER_GOODS.ORDER_ID=DASH_ORDER.ID " +
                        "GROUP by ORDER_ID )";
        return template.update(sql);
    }


    @Transactional
    public void delete(Long id) {
        OrderEntity entity = find(id);
        if( isCurrentUserGrantForDelete() || isOrderCreatorCanDelete(entity) ) {
            repository.delete(entity);
        }
    }

    private boolean isOrderCreatorCanDelete(OrderEntity entity) {
        return entity.isEditable() && userManager.loadCurrentUser().getId().equals(entity.getCreator().getId());
    }

    private boolean isCurrentUserGrantForDelete() {
        UserEntity userEntity = userManager.loadCurrentUser();
        return userEntity.getAuthorities().contains(PortalRole.ADMIN) ||
            userEntity.getAuthorities().contains(PortalRole.VERIFIER);
    }


    @Transactional(readOnly = true)
    public long loadCountOfUserOrder() {
        return getMyRepo().countByCreator(userManager.loadCurrentUser());
    }


    @Transactional
    public void discount(Long id, Long discount) {
        getMyRepo().setDiscount(id, discount);
    }

    @Transactional
    public void accept(Long id, String description) {
        loadInRegistrationState(id).ifPresent(en -> {
            en.setExtraDescription( description );
            putToState(orderStateManager.nextOf(en.getCurrentState()), en);
        });
    }

    private Optional<OrderEntity> loadInRegistrationState(Long id) {
        OrderEntity orderEntity = find(id);
        OrderStateEntity currentState = orderEntity.getCurrentState();
        if (currentState.getCode().equals(OrderStateManager.REGISTRATION_CODE)) {
            return Optional.of(orderEntity);
        } else {
            return Optional.empty();
        }
    }



    @Transactional
    public void reject(Long id, String description) {
        loadInRegistrationState(id).ifPresent( en -> {
            en.setExtraDescription( description );
            putToState(orderStateManager.loadRejectState(), en);
        });
    }
}
