package ir.fassih.workshopautomation.repository;

import ir.fassih.workshopautomation.entity.notification.NotificationEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends AbstractRepository<NotificationEntity, Long> {

}
