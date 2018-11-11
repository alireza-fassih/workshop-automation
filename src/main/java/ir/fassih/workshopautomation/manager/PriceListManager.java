package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.repository.GoodsRepository;
import ir.fassih.workshopautomation.repository.PriceListRepository;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PriceListManager {

    private PriceListRepository repository;
    private GoodsManager goodsManager;


    @Transactional
    public void calculateList() {
        goodsManager.loadNotDeletes()
            .stream().map( this::createAllPossibleState );


    }


    private List<State> createAllPossibleState( GoodsEntity entity ) {

        return null;
    }


    @Value
    public static class State {

    }
}
