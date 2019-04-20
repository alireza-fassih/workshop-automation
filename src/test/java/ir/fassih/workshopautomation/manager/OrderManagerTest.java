package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.order.OrderGoodsEntity;
import ir.fassih.workshopautomation.repository.GoodsRepository;
import ir.fassih.workshopautomation.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderManagerTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private GoodsRepository goodsRepo;


    @Test
    public void test_calculateUnitBeforSaveEntity() {
        GoodsEntity p1 = GoodsEntity.builder()
            .title("product 1")
                .width(0.1F)
                    .build();

        GoodsEntity p2 = GoodsEntity.builder()
            .title("product 2")
                .width(0.2F)
                    .build();

        goodsRepo.saveAll(Arrays.asList(p1, p2));


        OrderGoodsEntity o1 = OrderGoodsEntity.builder()
            .goods(p2)
                .count(1L)
                    .build();


        OrderGoodsEntity o2 = OrderGoodsEntity.builder()
            .goods(p1)
                .count(2L)
                    .build();

        OrderEntity order = OrderEntity.builder()
            .items(Arrays.asList(o1, o2))
                .build();


        orderManager.save( order );

        assertEquals( 0.4f, order.getUnit(), 0.0);
    }



}