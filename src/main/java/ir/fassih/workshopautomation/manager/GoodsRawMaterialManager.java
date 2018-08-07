package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import ir.fassih.workshopautomation.repository.GoodsRawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsRawMaterialManager extends AbstractManager<GoodsRawMaterialEntity, Long> {

    @Autowired
    public GoodsRawMaterialManager(GoodsRawMaterialRepository repository) {
        super(repository);
    }
}
