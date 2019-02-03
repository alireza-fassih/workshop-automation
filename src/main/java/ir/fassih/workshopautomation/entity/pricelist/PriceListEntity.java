package ir.fassih.workshopautomation.entity.pricelist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.fassih.workshopautomation.entity.core.Traceable;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "DASH_PRICE_LIST")
@Entity
public class PriceListEntity implements Traceable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name="CONTENT")
    @JsonIgnore
    private byte[] content;

    @Transient
    private String contentId;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "LAST_MODIFICATION_DATE")
    private Date lastModificationDate;

    @Column(name = "TITLE")
    private String title;
}
