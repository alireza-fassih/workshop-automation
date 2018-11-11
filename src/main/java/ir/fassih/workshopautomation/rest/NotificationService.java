package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.entity.notification.NotificationEntity;
import ir.fassih.workshopautomation.manager.AbstractManager;
import ir.fassih.workshopautomation.manager.NotificationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Secured("ADMIN")
@RestController
@RequestMapping("/rest/notifications")
public class NotificationService extends AbstractRestService<NotificationEntity, Long> {

    @Autowired
    public NotificationService(NotificationManager manager) {
        super(manager);
    }


}
