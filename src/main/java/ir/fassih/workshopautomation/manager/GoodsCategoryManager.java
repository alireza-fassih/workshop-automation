package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.goodscategory.GoodsCategoryEntity;
import ir.fassih.workshopautomation.entity.rawmaterialcategory.RawMaterialCategoryEntity;
import ir.fassih.workshopautomation.repository.GoodsCategoryRepository;
import ir.fassih.workshopautomation.repository.RawMaterialCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsCategoryManager extends  AbstractManager<GoodsCategoryEntity, Long> {

    @Autowired
    public GoodsCategoryManager(GoodsCategoryRepository repository) {
        super(repository);
    }

}
