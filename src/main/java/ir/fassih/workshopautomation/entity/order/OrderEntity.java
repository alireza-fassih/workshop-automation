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
import java.util.stream.Collectors;

@Data
@Table(name = "DASH_ORDER")
@Entity
public class OrderEntity implements Traceable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "CREATOR")
    private UserEntity creator;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private Collection<OrderGoodsEntity> items;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private Collection<StateOfOrderEntity> states;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "LAST_MODIFICATION_DATE")
    private Date lastModificationDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Transient
    public boolean isEditable() {
        Calendar calStart = new GregorianCalendar();
        calStart.setTime(new Date());
        calStart.set(Calendar.HOUR_OF_DAY, 0);
        calStart.set(Calendar.MINUTE, 0);
        calStart.set(Calendar.SECOND, 0);
        calStart.set(Calendar.MILLISECOND, 0);
        OrderStateEntity currentState = getCurrentState();
        return calStart.getTime().compareTo(Optional.ofNullable(getCreateDate()).orElse(new Date())) < 1 &&
                currentState != null && currentState.getParent() == null;
    }

    @ManyToOne
    @JoinColumn(name = "CURRENT_STATE_ID")
    private OrderStateEntity currentState;


    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getTitle() {
        return String.join(",", Optional.ofNullable(getItems()).orElse(new ArrayList<>()).stream()
                .map(OrderGoodsEntity::getGoodsTitle)
                .collect(Collectors.toList()));
    }

    public void putToState(StateOfOrderEntity state) {
        if (states == null) {
            states = new ArrayList<>();
        }
        states.add(state);
        setCurrentState(state.getState());
    }

}
