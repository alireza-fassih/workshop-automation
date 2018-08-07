package ir.fassih.workshopautomation.entity.goodsrawmaterial;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.rawmaterial.RawMaterialEntity;
import ir.fassih.workshopautomation.entity.rawmaterialcategory.RawMaterialCategoryEntity;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Optional;

@Data
@Table(name = "DASH_GOODS_RAW_MATERIAL")
@Entity
@Accessors(chain = true)
public class GoodsRawMaterialEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;


    @Column(name = "TITLE")
    private String title;

    @Column(name="SELECT_ABLE")
    private boolean selectAble;

    @ManyToOne
    @JoinColumn(name = "MATERIAL")
    private RawMaterialEntity material;

    @ManyToOne
    @JoinColumn(name = "CATEGORY")
    private RawMaterialCategoryEntity category;


    @ManyToOne
    @JoinColumn(name = "GOODS")
    @Getter(onMethod = @__(@JsonIgnore))
    private GoodsEntity goods;

    @Transient
    private String goodsName;

    @Column(name = "IMPORT_FACTOR")
    private Float importFactor;


    public String getGoodsName() {
        return Optional.ofNullable( goods )
            .map( GoodsEntity::getTitle ).orElse( "" );
    }

}
