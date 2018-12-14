package ir.fassih.workshopautomation.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.OptionalInt;

@Data
@Table(name = "DASH_ORDER_GOODS")
@Entity
public class OrderGoodsEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;


    @ManyToOne
    @JoinColumn(name = "GOODS")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private GoodsEntity goods;


    @Column(name = "ITEM_COUNT")
    private Long count;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private Collection<OrderItemEntity> items;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getGoodsTitle() {
        return Optional.ofNullable( goods )
            .map( GoodsEntity::getTitle ).orElse( "" );
    }

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Long getGoodId() {
        return Optional.ofNullable( goods )
            .map( GoodsEntity::getId ).orElse( null );
    }

    public Long calculatePrice() {
        return Optional.ofNullable(items).orElseGet(Collections::emptyList)
            .stream().map(OrderItemEntity::getPrice).reduce(0L, (agg, price) -> agg + price ) *
                Optional.ofNullable( count ).orElse( 1L );
    }
}
