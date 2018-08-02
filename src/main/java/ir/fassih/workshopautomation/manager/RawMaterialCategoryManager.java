package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.rawmaterialcategory.RawMaterialCategoryEntity;
import ir.fassih.workshopautomation.repository.RawMaterialCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RawMaterialCategoryManager extends  AbstractManager<RawMaterialCategoryEntity, Long> {

    @Autowired
    public RawMaterialCategoryManager(RawMaterialCategoryRepository repository) {
        super(repository);
    }

}
