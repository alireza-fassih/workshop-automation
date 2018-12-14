package ir.fassih.workshopautomation.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.fassih.workshopautomation.entity.core.Traceable;
import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import ir.fassih.workshopautomation.entity.orderstate.OrderStateEntity;
import ir.fassih.workshopautomation.entity.user.UserEntity;
import ir.fassih.workshopautomation.manager.OrderStateManager;
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
        OrderStateEntity state = getCurrentState();
        return calStart.getTime().compareTo(Optional.ofNullable(getCreateDate()).orElse(new Date())) < 1 &&
               state != null && OrderStateManager.EDIT_ABLE_STATES.contains(state.getCode()) ;
    }

    @ManyToOne
    @JoinColumn(name = "CURRENT_STATE_ID")
    private OrderStateEntity currentState;


    @Column(name = "DISCOUNT")
    private Long discount;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Long getTotlaPrice() {
        return Optional.ofNullable(items).orElseGet(Collections::emptyList)
            .stream().map(OrderGoodsEntity::calculatePrice)
            .reduce( 0L, (agg, price) -> agg + price) - Optional.ofNullable(discount).orElse(0L);
    }

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
