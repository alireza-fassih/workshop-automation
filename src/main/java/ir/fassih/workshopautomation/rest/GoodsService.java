package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.rawmaterial.RawMaterialEntity;
import ir.fassih.workshopautomation.entity.rawmaterialcategory.RawMaterialCategoryEntity;
import ir.fassih.workshopautomation.manager.AbstractManager;
import ir.fassih.workshopautomation.manager.GoodsManager;
import ir.fassih.workshopautomation.manager.OrderStateManager;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/rest/goods")
public class GoodsService extends AbstractRestService<GoodsEntity, Long> {

    @Autowired
    private OrderStateManager orderStateManager;

    @Autowired
    public GoodsService(GoodsManager manager) {
        super(manager);
    }


    private GoodsManager getMyManager() {
        return ( GoodsManager ) manager;
    }


    @Override
    protected Map<String, Object> optionsInternal() {
        Map<String, Object> props = super.optionsInternal();
        props.put("categories", getMyManager().loadAllCategories());
        props.put("states", orderStateManager.loadFirstStates());
        return props;
    }
}
