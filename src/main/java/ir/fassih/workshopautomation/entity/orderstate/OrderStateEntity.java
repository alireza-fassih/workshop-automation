package ir.fassih.workshopautomation.entity.orderstate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Optional;

@Data
@Table(name = "DASH_ORDER_STATE")
@Entity
public class OrderStateEntity {


    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "TITLE")
    private String title;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "PARENT")
    private OrderStateEntity parent;


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
