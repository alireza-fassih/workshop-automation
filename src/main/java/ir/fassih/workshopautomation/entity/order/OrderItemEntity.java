package ir.fassih.workshopautomation.entity.order;


import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.rawmaterial.RawMaterialEntity;
import ir.fassih.workshopautomation.entity.user.UserEntity;
import lombok.Data;

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
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;


    @ManyToOne
    @JoinColumn(name = "RAW_MATERIAL")
    private RawMaterialEntity rawMaterial;


}
