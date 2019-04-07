package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.orderstate.OrderStateEntity;
import ir.fassih.workshopautomation.repository.OrderStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderStateManager extends AbstractManager<OrderStateEntity, Long> {


    final static String REGISTRATION_CODE = "REGISTRATION";
    final static String REJECTED_CODE = "REJECTED";

    public final static List<String> EDIT_ABLE_STATES =
            Arrays.asList(REGISTRATION_CODE, REJECTED_CODE);


    @Autowired
    public OrderStateManager(OrderStateRepository repository) {
        super(repository, OrderStateEntity.class);
    }


    private OrderStateRepository getMyRepo() {
        return (OrderStateRepository) repository;
    }


    @Transactional(readOnly = true)
    public OrderStateEntity loadFirstStates() {
        return getMyRepo().findOneByCode(REGISTRATION_CODE);
    }

    @Transactional(readOnly = true)
    public OrderStateEntity nextOf(OrderStateEntity entity) {
        return repository.findOne((root, query, cb) ->
                cb.and(cb.equal(root.get("parent"), entity.getId()),
                        cb.or(cb.notEqual(root.get("deleted"), Boolean.TRUE), cb.isNull(root.get("deleted")))))
                .orElseGet(null);
    }

    @Transactional(readOnly = true)
    public OrderStateEntity loadRejectState() {
        return getMyRepo().findOneByCode(REJECTED_CODE);
    }
}
