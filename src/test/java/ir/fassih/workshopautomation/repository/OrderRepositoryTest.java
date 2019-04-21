package ir.fassih.workshopautomation.repository;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.order.OrderGoodsEntity;
import ir.fassih.workshopautomation.manager.OrderManager;
import ir.fassih.workshopautomation.repository.report.CountByTimeModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderRepositoryTest {


    @Autowired
    private OrderManager orderManager;

    @Autowired
    private GoodsRepository goodsRepo;

    @Autowired
    private OrderRepository repository;


    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Test
    public void test_calculateUnitBeforSaveEntity() throws ParseException {
        GoodsEntity p1 = GoodsEntity.builder()
                .title("product 1")
                .width(0.1F)
                .build();

        GoodsEntity p2 = GoodsEntity.builder()
                .title("product 2")
                .width(0.2F)
                .build();

        goodsRepo.saveAll(Arrays.asList(p1, p2));


        orderManager.save(createOrder("2019-10-10 10:10", p1 ,p2, p2));

        orderManager.save(createOrder("2019-10-11 10:10", p1 ,p2, p2)); // 0.5
        orderManager.save(createOrder("2019-10-11 11:10", p1 ,p2, p2)); // 0.5
        orderManager.save(createOrder("2019-10-11 12:10", p1 ,p2, p2)); // 0.5
        orderManager.save(createOrder("2019-10-11 13:10", p1 ,p2, p2)); // 0.5
        orderManager.save(createOrder("2019-10-11 14:10", p1 ,p2, p2)); // 0.5
        orderManager.save(createOrder("2019-10-11 15:10", p1 ,p2, p2)); // 0.5


        orderManager.save(createOrder("2019-10-12 10:10", p1 ,p2, p2)); // 0.5
        orderManager.save(createOrder("2019-10-12 11:10", p1 ,p2));     // 0.3
        orderManager.save(createOrder("2019-10-12 12:10", p1 ,p2));     // 0.3


        orderManager.save(createOrder("2019-10-13 11:10", p1 ,p2));    // 0.3
        orderManager.save(createOrder("2019-10-13 12:10", p1 ,p2));    // 0.3

        orderManager.save(createOrder("2019-10-14 14:10", p1 ,p2, p2));
        orderManager.save(createOrder("2019-10-14 15:10", p1 ,p2, p2));


        List<CountByTimeModel<Double>> result =
                repository.reportByTime(parser.parse("2019-10-11 00:00"), parser.parse("2019-10-14 00:00"));

        assertEquals(3, result.size());


        result.forEach( r -> {
            Date date = r.getGroupedDate();
            Double count = r.getCount();
            try {
                if( date.equals( parser.parse("2019-10-11 00:00") ) ) {
                    assertEquals(3.0f, count, 0.0);
                } else if( date.equals( parser.parse("2019-10-12 00:00") ) ) {
                    assertEquals(1.1f, count, 0.0);
                } else if( date.equals( parser.parse("2019-10-13 00:00") ) ) {
                    assertEquals(0.6f, count, 0.0);
                } else {
                    fail("unknown date");
                }
            } catch (ParseException e) {
                throw new IllegalStateException(e);
            }
        });
    }


    private OrderEntity createOrder(String createDate, GoodsEntity... products) throws ParseException {

        Map<Long, List<GoodsEntity>> items = Stream.of(products)
                .collect(Collectors.groupingBy(GoodsEntity::getId));

        OrderEntity order = OrderEntity.builder()
                .items(new ArrayList<>())
                .createDate(parser.parse(createDate))
                .build();

        items.forEach((k, v) -> {
            order.getItems().add(
                    OrderGoodsEntity.builder()
                            .order(order)
                            .goods(v.get(0))
                            .count((long) v.size())
                            .build());
        });

        return order;
    }
}