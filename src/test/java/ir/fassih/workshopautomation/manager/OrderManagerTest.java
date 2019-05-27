package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.entity.goods.GoodsEntity;
import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.order.OrderGoodsEntity;
import ir.fassih.workshopautomation.repository.GoodsRepository;
import ir.fassih.workshopautomation.repository.OrderRepository;
import ir.fassih.workshopautomation.repository.report.AbstractReportModel;
import ir.fassih.workshopautomation.rest.model.ReportOrderUnitByTime;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderManagerTest {

    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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


    @Test
    public void test_reportWithCreateDate() throws ParseException {
        GoodsEntity p1 = GoodsEntity.builder()
                .title("product 1")
                .width(0.1F)
                .build();

        GoodsEntity p2 = GoodsEntity.builder()
                .title("product 2")
                .width(0.2F)
                .build();

        goodsRepo.saveAll(Arrays.asList(p1, p2));


        orderManager.save(createOrder("2019-10-10 10:10", p1 ,p2, p2)); // 0.5

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

        orderManager.save(createOrder("2019-10-14 14:10", p1 ,p2, p2)); // 0.5
        orderManager.save(createOrder("2019-10-14 15:10", p1 ,p2, p2)); // 0.5


        ReportOrderUnitByTime model = new ReportOrderUnitByTime();
        model.handleUnknown("GE:createDate", parser.parse("2019-10-11 00:00").getTime());
        model.handleUnknown("LE:createDate", parser.parse("2019-10-14 00:00").getTime());

        model.setRange(ReportOrderUnitByTime.DateRange.DAY);


        List<AbstractReportModel.DoubleReportModel> result =
                orderManager.report(model);

        assertEquals(3, result.size());


        result.forEach( r -> {
            Date date = r.getDate();
            Double count = r.getData();
            try {
                if( date.equals( parser.parse("2019-10-11 00:00") ) ) {
                    assertEquals(3.0f, count, 0.0);
                } else if( date.equals( parser.parse("2019-10-12 00:00") ) ) {
                    assertEquals(1.1f, count, 0.0);
                } else if( date.equals( parser.parse("2019-10-13 00:00") ) ) {
                    assertEquals(0.6f, count, 0.0);
                } else {
                    fail("unknown date " + date);
                }
            } catch (ParseException e) {
                throw new IllegalStateException(e);
            }
        });
    }





    @Test
    public void test_testWithoutFilter() throws ParseException {
        GoodsEntity p1 = GoodsEntity.builder()
                .title("product 1")
                .width(0.1F)
                .build();

        GoodsEntity p2 = GoodsEntity.builder()
                .title("product 2")
                .width(0.2F)
                .build();

        goodsRepo.saveAll(Arrays.asList(p1, p2));


        orderManager.save(createOrder("2019-10-10 10:10", p1 ,p2, p2)); // 0.5

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

        orderManager.save(createOrder("2019-10-14 14:10", p1 ,p2, p2)); // 0.5
        orderManager.save(createOrder("2019-10-14 15:10", p1 ,p2, p2)); // 0.5


        ReportOrderUnitByTime model = new ReportOrderUnitByTime();
        model.setRange(ReportOrderUnitByTime.DateRange.DAY);


        List<AbstractReportModel.DoubleReportModel> result =
                orderManager.report(model);

        assertEquals(5, result.size());


        result.forEach( r -> {
            Date date = r.getDate();
            Double count = r.getData();
            try {
                if( date.equals( parser.parse("2019-10-11 00:00") ) ) {
                    assertEquals(3.0f, count, 0.0);
                } else if( date.equals( parser.parse("2019-10-12 00:00") ) ) {
                    assertEquals(1.1f, count, 0.0);
                } else if( date.equals( parser.parse("2019-10-13 00:00") ) ) {
                    assertEquals(0.6f, count, 0.0);
                } else if ( date.equals( parser.parse("2019-10-10 00:00") ) ) {
                    assertEquals(0.5f, count, 0.0);
                } else if ( date.equals( parser.parse("2019-10-14 00:00") ) ) {
                    assertEquals(1.0f, count, 0.0);
                } else {
                    fail("unknown date " + date);
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