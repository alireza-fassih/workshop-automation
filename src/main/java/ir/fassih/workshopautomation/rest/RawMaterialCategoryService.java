package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.entity.rawmaterialcategory.RawMaterialCategoryEntity;
import ir.fassih.workshopautomation.manager.AbstractManager;
import ir.fassih.workshopautomation.manager.RawMaterialCategoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/rawMaterialCategory")
public class RawMaterialCategoryService extends  AbstractRestService<RawMaterialCategoryEntity, Long> {

    @Autowired
    public RawMaterialCategoryService(RawMaterialCategoryManager manager) {
        super(manager);
    }


}
