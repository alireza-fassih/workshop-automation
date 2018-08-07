package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import ir.fassih.workshopautomation.repository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

@Service
public class GoodsManager extends AbstractManager<GoodsEntity, Long> {


    @Autowired
    public GoodsManager(GoodsRepository repository) {
        super(repository);
    }



    @Transactional(readOnly = true)
    public Collection<GoodsRawMaterialEntity> loadGoodsMetadata(Long id) {
        return repository.findOne(id).getRawMaterials();
    }
}
