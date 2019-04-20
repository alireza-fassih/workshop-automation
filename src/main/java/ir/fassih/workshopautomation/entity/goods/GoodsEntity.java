package ir.fassih.workshopautomation.entity.goods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.fassih.workshopautomation.entity.core.LogicallyDeletable;
import ir.fassih.workshopautomation.entity.core.Traceable;
import ir.fassih.workshopautomation.entity.goodscategory.GoodsCategoryEntity;
import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import ir.fassih.workshopautomation.entity.orderstate.OrderStateEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Data
@Table(name = "DASH_GOODS")
@Entity
@ToString(exclude = "rawMaterials")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsEntity implements LogicallyDeletable , Traceable {


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


    @Column(name = "TITLE")
    private String title;

    @Column(name = "DELETED")
    private Boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "CATEGORY")
    private GoodsCategoryEntity category;


    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "LAST_MODIFICATION_DATE")
    private Date lastModificationDate;


    @Getter(onMethod = @__(@JsonIgnore))
    @OneToMany(mappedBy = "goods")
    private Collection<GoodsRawMaterialEntity> rawMaterials;


    @ManyToOne
    @JoinColumn(name = "FIRST_STATE")
    private OrderStateEntity firstState;


    @Basic
    @Column(name = "WIDTH")
    private Float width;

}
