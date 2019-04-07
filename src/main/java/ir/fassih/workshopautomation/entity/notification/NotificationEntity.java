package ir.fassih.workshopautomation.entity.notification;

import ir.fassih.workshopautomation.entity.core.LogicallyDeletable;
import ir.fassih.workshopautomation.entity.core.Traceable;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;


@Data
@Table(name = "DASH_NOTIFICATIONS")
@Entity
public class NotificationEntity implements LogicallyDeletable, Traceable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    protected Long id;

    @Column(name="CONTENT")
    private String content;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name="DELETED")
    private Boolean deleted;

    @Column(name = "LAST_MODIFICATION_DATE")
    private Date lastModificationDate;

}
