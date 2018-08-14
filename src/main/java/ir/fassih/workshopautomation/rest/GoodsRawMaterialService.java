package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.entity.goodsrawmaterial.GoodsRawMaterialEntity;
import ir.fassih.workshopautomation.manager.GoodsRawMaterialManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/rest/goodsRawMaterial")
public class GoodsRawMaterialService extends AbstractRestService<GoodsRawMaterialEntity, Long> {
    @Autowired
    public GoodsRawMaterialService(GoodsRawMaterialManager manager) {
        super(manager);
    }

    private GoodsRawMaterialManager getMyManager() {
        return (GoodsRawMaterialManager) manager;
    }

    @Override
    protected Map<String, Object> optionsInternal() {
        return getMyManager().createOptions();
    }
}
