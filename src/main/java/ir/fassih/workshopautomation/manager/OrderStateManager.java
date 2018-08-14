package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.orderstate.OrderStateEntity;
import ir.fassih.workshopautomation.repository.OrderStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderStateManager extends AbstractManager<OrderStateEntity, Long> {

    @Autowired
    public OrderStateManager(OrderStateRepository repository) {
        super(repository);
    }


    @Transactional(readOnly = true)
    public Iterable<OrderStateEntity> loadFirstStates() {
        return repository.findAll(( root, q, cb) -> cb.isNull( root.get("parent") ));
    }
}
