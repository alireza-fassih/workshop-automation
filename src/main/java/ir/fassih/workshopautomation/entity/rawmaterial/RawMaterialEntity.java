package ir.fassih.workshopautomation.entity.rawmaterial;

import javax.persistence.*;

import groovy.transform.EqualsAndHashCode;
import ir.fassih.workshopautomation.entity.core.AbstractBaseEntity;
import ir.fassih.workshopautomation.entity.core.LogicallyDeletable;
import ir.fassih.workshopautomation.entity.core.Traceable;
import ir.fassih.workshopautomation.entity.rawmaterialcategory.RawMaterialCategoryEntity;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "DASH_RAW_MATERIAL")
public class RawMaterialEntity implements LogicallyDeletable, Traceable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DELETED")
    private boolean deleted = false;

    @Column(name = "UNIT_PRICE")
    private Long unitPrice;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "LAST_MODIFICATION_DATE")
    private Date lastModificationDate;

    @ManyToOne
    @JoinColumn(name="CATEGORY")
    private RawMaterialCategoryEntity category;
}
