package ir.fassih.workshopautomation.repository;

import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsRepository extends  AbstractRepository<GoodsEntity, Long> {
}
