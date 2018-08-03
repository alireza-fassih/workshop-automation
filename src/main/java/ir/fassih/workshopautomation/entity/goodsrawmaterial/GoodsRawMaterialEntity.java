package ir.fassih.workshopautomation.entity.goodsrawmaterial;

import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.rawmaterial.RawMaterialEntity;
import ir.fassih.workshopautomation.entity.rawmaterialcategory.RawMaterialCategoryEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "DASH_GOODS_RAW_MATERIAL")
@Entity
public class GoodsRawMaterialEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

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
