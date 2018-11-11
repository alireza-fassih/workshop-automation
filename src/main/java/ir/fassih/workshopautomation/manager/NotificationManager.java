package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.notification.NotificationEntity;
import ir.fassih.workshopautomation.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class NotificationManager extends AbstractManager<NotificationEntity, Long> {



    @Autowired
    public NotificationManager(NotificationRepository repository) {
        super(repository, NotificationEntity.class);
    }


    private NotificationRepository getMyRepo() {
        return (NotificationRepository) repository;
    }


    @Transactional(readOnly = true)
    public List<NotificationEntity> getTop5() {
        return repository.findAll( (root, query, cb ) -> {
            query.orderBy(cb.desc(root.get("lastModificationDate")));
            return cb.or(cb.notEqual(root.get("deleted"), Boolean.TRUE), cb.isNull(root.get("deleted")));
        }, new PageRequest(0,5)).getContent();
    }

}
