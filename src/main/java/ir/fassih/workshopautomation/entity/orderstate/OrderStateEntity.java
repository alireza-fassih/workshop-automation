package ir.fassih.workshopautomation.entity.orderstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.fassih.workshopautomation.entity.core.LogicallyDeletable;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Optional;

@Data
@Table(name = "DASH_ORDER_STATE")
@Entity
public class OrderStateEntity implements LogicallyDeletable {


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

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DELETED")
    private Boolean deleted = Boolean.FALSE;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "PARENT")
    private OrderStateEntity parent;


    @Column(name = "CODE")
    private String code;


    @Column(name = "COLOR")
    private String color;

    @Transient
    public String getParentTitle() {
        return Optional.ofNullable(parent).map(OrderStateEntity::getTitle)
                .orElse(null);
    }

    @Transient
    public Long getParentId() {
        return Optional.ofNullable(parent).map(OrderStateEntity::getId)
                .orElse(null);
    }
}
