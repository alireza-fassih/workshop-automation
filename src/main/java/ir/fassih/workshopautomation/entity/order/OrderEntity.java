package ir.fassih.workshopautomation.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.fassih.workshopautomation.entity.core.Traceable;
import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import ir.fassih.workshopautomation.entity.orderstate.OrderStateEntity;
import ir.fassih.workshopautomation.entity.user.UserEntity;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Data
@Table(name = "DASH_ORDER")
@Entity
public class OrderEntity implements Traceable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "TITLE")
    private String title;

    @ManyToOne
    @JoinColumn(name = "CREATOR")
    private UserEntity creator;


    @ManyToOne
    @JoinColumn(name = "GOODS")
    @JsonIgnore
    private GoodsEntity goods;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private Collection<OrderItemEntity> items;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private Collection<StateOfOrderEntity> states;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "LAST_MODIFICATION_DATE")
    private Date lastModificationDate;

    @Transient
    public boolean isEditable() {
        Calendar calStart = new GregorianCalendar();
        calStart.setTime(new Date());
        calStart.set(Calendar.HOUR_OF_DAY, 0);
        calStart.set(Calendar.MINUTE, 0);
        calStart.set(Calendar.SECOND, 0);
        calStart.set(Calendar.MILLISECOND, 0);
        OrderStateEntity currentState = getCurrentState();
        return calStart.getTime().equals(createDate) &&
            currentState != null && currentState.getParent() == null;
    }

    @ManyToOne
    @JoinColumn(name = "CURRENT_STATE_ID")
    private OrderStateEntity currentState;

    public void putToState(StateOfOrderEntity state) {
        if (states == null) {
            states = new ArrayList<>();
        }
        states.add(state);
        setCurrentState(state.getState());
    }
}
