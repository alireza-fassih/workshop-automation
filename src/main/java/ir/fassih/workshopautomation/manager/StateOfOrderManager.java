package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.order.StateOfOrderEntity;
import ir.fassih.workshopautomation.entity.user.UserEntity;
import ir.fassih.workshopautomation.repository.AbstractRepository;
import ir.fassih.workshopautomation.repository.StateOfOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class StateOfOrderManager extends AbstractManager<StateOfOrderEntity, Long> {

    @Autowired
    UserManager userManager;

    @Autowired
    public StateOfOrderManager(StateOfOrderRepository repository) {
        super(repository, StateOfOrderEntity.class);
    }

    @Transactional(readOnly = true)
    public List<StateOfOrderEntity> loadLastSateOfUser() {
        UserEntity userEntity = userManager.loadCurrentUser();
        return repository.findAll((root, query, cb) -> {
            query.orderBy(cb.desc(root.get( "createDate" )));
            return cb.equal(root.get("order").get("creator"), userEntity);
        });
    }


}
