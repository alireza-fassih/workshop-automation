package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.entity.pricelist.PriceListEntity;
import ir.fassih.workshopautomation.manager.PriceListManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/price-list")
public class PriceListService extends AbstractRestService<PriceListEntity, Long> {

    @Autowired
    public PriceListService(PriceListManager manager) {
        super(manager);
    }
}
