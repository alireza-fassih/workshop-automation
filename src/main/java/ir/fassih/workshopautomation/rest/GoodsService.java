package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.manager.AbstractManager;
import ir.fassih.workshopautomation.manager.GoodsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/goods")
public class GoodsService extends AbstractRestService<GoodsEntity, Long> {

    @Autowired
    public GoodsService(GoodsManager manager) {
        super(manager);
    }

}
