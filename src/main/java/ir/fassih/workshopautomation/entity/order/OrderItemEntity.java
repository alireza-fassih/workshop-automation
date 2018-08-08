package ir.fassih.workshopautomation.entity.order;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import ir.fassih.workshopautomation.entity.rawmaterial.RawMaterialEntity;
import ir.fassih.workshopautomation.entity.user.UserEntity;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

@Data
@Table(name = "DASH_ORDER_ITEM")
@Entity
public class OrderItemEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "GOODS_RAW_MATERIAL")
    private GoodsRawMaterialEntity metadata;

    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "RAW_MATERIAL")
    private RawMaterialEntity material;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "PRICE")
    private Long price;

}
