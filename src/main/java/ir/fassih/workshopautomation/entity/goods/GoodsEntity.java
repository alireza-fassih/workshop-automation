package ir.fassih.workshopautomation.entity.goods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.fassih.workshopautomation.entity.core.LogicallyDeletable;
import ir.fassih.workshopautomation.entity.core.Traceable;
import ir.fassih.workshopautomation.entity.goodscategory.GoodsCategoryEntity;
import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Table(name = "DASH_GOODS")
@Entity
public class GoodsEntity implements LogicallyDeletable , Traceable {


    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;


    @Column(name = "TITLE")
    private String title;

    @Column(name = "DELETED")
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "CATEGORY")
    private GoodsCategoryEntity category;


    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "LAST_MODIFICATION_DATE")
    private Date lastModificationDate;


    @Getter(onMethod = @__(@JsonIgnore))
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "goods")
    private Set<GoodsRawMaterialEntity> rawMaterials;

}
