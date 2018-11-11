package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.entity.rawmaterial.RawMaterialEntity;
import ir.fassih.workshopautomation.manager.AbstractManager;
import ir.fassih.workshopautomation.manager.RawMaterialCategoryManager;
import ir.fassih.workshopautomation.manager.RawMaterialManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Secured("ADMIN")
@RestController
@RequestMapping("/rest/rawMaterial")
public class RawMaterialService extends AbstractRestService<RawMaterialEntity, Long> {

    private RawMaterialManager getMyManager() {
        return (RawMaterialManager) manager;
    }

    @Autowired
    public RawMaterialService(RawMaterialManager manager) {
        super(manager);
    }


    @Override
    protected Map<Class<? extends AbstractManager>, String> getOptionsMetadata() {
        Map<Class<? extends AbstractManager>, String> metadata = super.getOptionsMetadata();
        metadata.put(RawMaterialCategoryManager.class, "categories");
        return metadata;
    }

}
