package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.entity.rawmaterial.RawMaterialEntity;
import ir.fassih.workshopautomation.manager.AbstractManager;
import ir.fassih.workshopautomation.manager.RawMaterialManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/rawMaterial")
public class RawMaterialService extends AbstractRestService<RawMaterialEntity, Long> {

    @Autowired
    public RawMaterialService(RawMaterialManager manager) {
        super(manager);
    }


}
