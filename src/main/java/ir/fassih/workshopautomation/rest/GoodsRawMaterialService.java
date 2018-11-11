package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import ir.fassih.workshopautomation.manager.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Secured("ADMIN")
@RestController
@RequestMapping("/rest/goodsRawMaterial")
public class GoodsRawMaterialService extends AbstractRestService<GoodsRawMaterialEntity, Long> {

    @Autowired
    public GoodsRawMaterialService(GoodsRawMaterialManager manager) {
        super(manager);
    }

    @Override
    protected Map<Class<? extends AbstractManager>, String> getOptionsMetadata() {
        Map<Class<? extends AbstractManager>, String> metadata = super.getOptionsMetadata();
        metadata.put(RawMaterialCategoryManager.class, "categories");
        metadata.put(RawMaterialManager.class, "materials");
        metadata.put(GoodsManager.class, "products");
        return metadata;
    }

}
