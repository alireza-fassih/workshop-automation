package ir.fassih.workshopautomation.repository;

import ir.fassih.workshopautomation.entity.pricelist.PriceListEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceListRepository extends AbstractRepository<PriceListEntity, Long>{

    PriceListEntity findFirstOneByOrderByIdDesc();

}
