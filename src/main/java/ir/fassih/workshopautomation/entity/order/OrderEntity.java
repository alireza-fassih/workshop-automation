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
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Table(name = "DASH_ORDER")
@Entity
public class OrderEntity implements Traceable {

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

    @ManyToOne
    @JoinColumn(name = "CREATOR")
    private UserEntity creator;


    @Basic
    @Column(name =  "UNIT")
    private float unit;



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

    @Column(name = "EXTRA_DESCRIPTION")
    private String extraDescription;

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
        return Optional.ofNullable(getItems()).orElse(new ArrayList<>()).stream()
                .map(OrderGoodsEntity::getGoodsTitle)
                .collect(Collectors.joining("و"));
    }


    public void putToState(StateOfOrderEntity state) {
        if (states == null) {
            states = new ArrayList<>();
        }
        states.add(state);
        setCurrentState(state.getState());
    }

    public String createDetails() {
        return Optional.ofNullable(items).orElseGet(Collections::emptyList)
            .stream().map(OrderGoodsEntity::createDetails)
            .collect(Collectors.joining(" | "));
    }
}
