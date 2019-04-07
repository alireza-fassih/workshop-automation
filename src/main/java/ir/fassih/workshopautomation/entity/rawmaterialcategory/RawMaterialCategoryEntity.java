package ir.fassih.workshopautomation.entity.rawmaterialcategory;

import ir.fassih.workshopautomation.entity.core.LogicallyDeletable;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Table(name = "DASH_RAW_MATERIAL_CATEGORY")
@Entity
public class RawMaterialCategoryEntity implements LogicallyDeletable {

    private static final long serialVersionUID = 568192342343246285L;

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
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DELETED")
    private Boolean deleted = false;


    @Column(name = "ALLOW_DISCOUNT")
    private Boolean allowDiscount;

}
