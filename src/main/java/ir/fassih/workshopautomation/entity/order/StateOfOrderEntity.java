package ir.fassih.workshopautomation.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.fassih.workshopautomation.entity.orderstate.OrderStateEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "DASH_ORDER_TO_STATE")
@Entity
public class StateOfOrderEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "STATE_ID")
    private OrderStateEntity state;

    @Column(name = "CREATE_DATE")
    private Date createDate;

}
