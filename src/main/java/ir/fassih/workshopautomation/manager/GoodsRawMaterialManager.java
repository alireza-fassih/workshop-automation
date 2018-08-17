package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import ir.fassih.workshopautomation.repository.GoodsRawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class GoodsRawMaterialManager extends AbstractManager<GoodsRawMaterialEntity, Long> {

    @Autowired
    public GoodsRawMaterialManager(GoodsRawMaterialRepository repository) {
        super(repository, GoodsRawMaterialEntity.class);
    }


    @Override
    public void save(GoodsRawMaterialEntity entity) {
        entity.setSelectAble( entity.getMaterial() == null );
        super.save(entity);
    }


}
