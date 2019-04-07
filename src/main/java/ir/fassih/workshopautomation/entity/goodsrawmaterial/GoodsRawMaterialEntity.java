package ir.fassih.workshopautomation.entity.goodsrawmaterial;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.fassih.workshopautomation.entity.core.LogicallyDeletable;
import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.rawmaterial.RawMaterialEntity;
import ir.fassih.workshopautomation.entity.rawmaterialcategory.RawMaterialCategoryEntity;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Optional;

@Data
@Table(name = "DASH_GOODS_RAW_MATERIAL")
@Entity
public class GoodsRawMaterialEntity implements LogicallyDeletable {


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

    @Column(name = "DELETED")
    private Boolean deleted = Boolean.FALSE;


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
    private GoodsEntity goods;

    @Column(name = "IMPORT_FACTOR")
    private Float importFactor;


}
