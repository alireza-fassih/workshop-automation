package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.pricelist.PriceListEntity;
import ir.fassih.workshopautomation.repository.AbstractRepository;
import ir.fassih.workshopautomation.repository.GoodsRepository;
import ir.fassih.workshopautomation.repository.PriceListRepository;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class PriceListManager extends AbstractManager<PriceListEntity, Long> {

    @Autowired
    private UploaderManager uploaderManager;

    @Autowired
    public PriceListManager(PriceListRepository repository) {
        super(repository, PriceListEntity.class);
    }


    @Override
    public void save(PriceListEntity entity) {
        entity.setContent( uploaderManager.readFile( entity.getContentId() ));
        super.save(entity);
    }
}
