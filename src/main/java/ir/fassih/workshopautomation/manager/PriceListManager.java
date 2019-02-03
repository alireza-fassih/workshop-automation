package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.pricelist.PriceListEntity;
import ir.fassih.workshopautomation.repository.PriceListRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
public class PriceListManager extends AbstractManager<PriceListEntity, Long> {

    @Autowired
    private UploaderManager uploaderManager;

    @Autowired
    public PriceListManager(PriceListRepository repository) {
        super(repository, PriceListEntity.class);
    }


    @Override
    @Transactional
    public void save(PriceListEntity entity) {
        entity.setContent( uploaderManager.readFile( entity.getContentId() ));
        super.save(entity);
    }


    @Override
    @Transactional
    public void update(Long id, PriceListEntity entity) {
        PriceListEntity db = repository.findOne(id);
        BeanUtils.copyProperties(entity, db, "content");
        if(StringUtils.hasText(entity.getContentId())) {
            db.setContent( uploaderManager.readFile( entity.getContentId() ));
        }
        db.setLastModificationDate(new Date());
        save(db);
    }
}
