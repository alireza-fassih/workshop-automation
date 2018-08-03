package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.repository.AbstractRepository;
import ir.fassih.workshopautomation.repository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsManager  extends AbstractManager<GoodsEntity, Long> {

    @Autowired
    public GoodsManager(GoodsRepository repository) {
        super(repository);
    }

}
