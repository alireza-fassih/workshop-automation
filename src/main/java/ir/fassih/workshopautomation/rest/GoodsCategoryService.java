package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.entity.goodscategory.GoodsCategoryEntity;
import ir.fassih.workshopautomation.entity.rawmaterialcategory.RawMaterialCategoryEntity;
import ir.fassih.workshopautomation.manager.GoodsCategoryManager;
import ir.fassih.workshopautomation.manager.RawMaterialCategoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Secured("ADMIN")
@RestController
@RequestMapping("/rest/goodsCategory")
public class GoodsCategoryService extends  AbstractRestService<GoodsCategoryEntity, Long> {

    @Autowired
    public GoodsCategoryService(GoodsCategoryManager manager) {
        super(manager);
    }


}
