package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.entity.rawmaterial.RawMaterialEntity;
import ir.fassih.workshopautomation.manager.AbstractManager;
import ir.fassih.workshopautomation.manager.RawMaterialManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
    protected Map<String, Object> optionsInternal() {
        Map<String, Object> objects = super.optionsInternal();
        objects.put("categories", getMyManager().loadAllCategories());
        return objects;
    }
}
