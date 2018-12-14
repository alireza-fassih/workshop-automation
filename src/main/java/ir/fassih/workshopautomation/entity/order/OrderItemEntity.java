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
import java.util.Optional;

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
    @JoinColumn(name = "ORDER_GOODS")
    private OrderGoodsEntity order;

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


    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Long getMetadataId() {
        return Optional.ofNullable(metadata)
            .map(GoodsRawMaterialEntity::getId).orElse(null);
    }

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Long getMaterialId() {
        return Optional.ofNullable(material)
            .map(RawMaterialEntity::getId).orElse(null);
    }

    @Transient
    public Boolean isSelectable() {
        return Optional.ofNullable( metadata )
            .map(GoodsRawMaterialEntity::isSelectAble).orElse(null);
    }

    @Transient
    public String getRawMaterialTitle() {
        return Optional.ofNullable( material )
            .map( RawMaterialEntity::getTitle ).orElse( null );
    }


}
