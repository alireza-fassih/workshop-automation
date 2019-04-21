package ir.fassih.workshopautomation.repository;

import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.entity.order.StateOfOrderEntity;
import ir.fassih.workshopautomation.entity.orderstate.OrderStateEntity;
import ir.fassih.workshopautomation.entity.user.UserEntity;
import ir.fassih.workshopautomation.manager.OrderManager;
import ir.fassih.workshopautomation.manager.UserManager;
import ir.fassih.workshopautomation.repository.report.CountByTimeModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StateOfOrderRepositoryTest {


    @Autowired
    private StateOfOrderRepository repo;

    @Autowired
    private OrderStateRepository orderStateRepository;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private UserManager userManager;



    @Test
    public void testReportWithStateAndUser() throws ParseException {
        UserEntity u = new UserEntity();
        u.setUsername("111");
        u.setPassword("111");
        userManager.save(u);

        UserEntity u2 = new UserEntity();
        u2.setUsername("222");
        u2.setPassword("11221");
        userManager.save(u2);



        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCreator(u);
        orderManager.save( orderEntity );


        OrderEntity orderEntity2 = new OrderEntity();
        orderEntity2.setCreator(u2);
        orderManager.save( orderEntity2 );

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        OrderStateEntity os = new OrderStateEntity();
        os.setTitle( "123");
        os.setCode("123");

        orderStateRepository.save(os);


        repo.save( createState(parser.parse("2016-10-10 11:22"), os, orderEntity) );

        repo.save( createState(parser.parse("2016-10-11 11:22"), os, orderEntity) );

        repo.save( createState(parser.parse("2016-10-12 11:22"), os, orderEntity) );
        repo.save( createState(parser.parse("2016-10-12 12:22"), os, orderEntity) );
        repo.save( createState(parser.parse("2016-10-12 12:42"), os, orderEntity) );
        repo.save( createState(parser.parse("2016-10-12 13:22"), os, orderEntity) );



        repo.save( createState(parser.parse("2016-10-13 11:22"), os, orderEntity) );
        repo.save( createState(parser.parse("2016-10-13 11:22"), os, orderEntity) );
        repo.save( createState(parser.parse("2016-10-13 11:22"), os, orderEntity) );


        repo.save( createState(parser.parse("2016-10-14 11:22"), os, orderEntity) );

        repo.save( createState(parser.parse("2016-10-15 11:22"), os, orderEntity) );
        repo.save( createState(parser.parse("2016-10-15 11:22"), os, orderEntity) );
        repo.save( createState(parser.parse("2016-10-15 11:22"), os, orderEntity) );




        repo.save( createState(parser.parse("2016-10-10 11:22"), os, orderEntity2) );

        repo.save( createState(parser.parse("2016-10-11 11:22"), os, orderEntity2) );

        repo.save( createState(parser.parse("2016-10-12 11:22"), os, orderEntity2) );
        repo.save( createState(parser.parse("2016-10-12 12:22"), os, orderEntity2) );
        repo.save( createState(parser.parse("2016-10-12 12:42"), os, orderEntity2) );
        repo.save( createState(parser.parse("2016-10-12 13:22"), os, orderEntity2) );



        repo.save( createState(parser.parse("2016-10-13 11:22"), os, orderEntity2) );
        repo.save( createState(parser.parse("2016-10-13 11:22"), os, orderEntity2) );
        repo.save( createState(parser.parse("2016-10-13 11:22"), os, orderEntity2) );


        repo.save( createState(parser.parse("2016-10-14 11:22"), os, orderEntity2) );

        repo.save( createState(parser.parse("2016-10-15 11:22"), os, orderEntity2) );
        repo.save( createState(parser.parse("2016-10-15 11:22"), os, orderEntity2) );
        repo.save( createState(parser.parse("2016-10-15 11:22"), os, orderEntity2) );


        List<CountByTimeModel<Long>> result =
                repo.reportByStateAndUser(os.getId(), u.getId(), parser.parse("2016-10-11 00:00"), parser.parse("2016-10-15 00:00"));


        assertEquals(4, result.size());

        for (CountByTimeModel<Long> r : result ) {
            Date groupedDate = r.getGroupedDate();
            if( groupedDate.equals( parser.parse("2016-10-11 00:00") ) ) {
                assertEquals(1, r.getCount().longValue());
            } else if( groupedDate.equals( parser.parse("2016-10-12 00:00") ) ) {
                assertEquals(4, r.getCount().longValue());
            } else if( groupedDate.equals( parser.parse("2016-10-13 00:00") ) ) {
                assertEquals(3, r.getCount().longValue());
            } else if( groupedDate.equals( parser.parse("2016-10-14 00:00") ) ) {
                assertEquals(1, r.getCount().longValue());
            } else {
                fail("should go in one of those");
            }
        }


    }


    @Test
    public void testInsertWithoutUser() throws ParseException {
        OrderStateEntity os = new OrderStateEntity();
        os.setTitle( "123");
        os.setCode("123");

        orderStateRepository.save(os);

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm");


        repo.save( createState(parser.parse("2016-10-10 11:22"), os) );

        repo.save( createState(parser.parse("2016-10-11 11:22"), os) );

        repo.save( createState(parser.parse("2016-10-12 11:22"), os) );
        repo.save( createState(parser.parse("2016-10-12 12:22"), os) );
        repo.save( createState(parser.parse("2016-10-12 12:42"), os) );
        repo.save( createState(parser.parse("2016-10-12 13:22"), os) );



        repo.save( createState(parser.parse("2016-10-13 11:22"), os) );
        repo.save( createState(parser.parse("2016-10-13 11:22"), os) );
        repo.save( createState(parser.parse("2016-10-13 11:22"), os) );


        repo.save( createState(parser.parse("2016-10-14 11:22"), os) );

        repo.save( createState(parser.parse("2016-10-15 11:22"), os) );
        repo.save( createState(parser.parse("2016-10-15 11:22"), os) );
        repo.save( createState(parser.parse("2016-10-15 11:22"), os) );


        List<CountByTimeModel<Long>> result =
            repo.reportByState(os.getId(), parser.parse("2016-10-11 00:00"), parser.parse("2016-10-15 00:00"));


        assertEquals(4, result.size());

        for (CountByTimeModel<Long> r : result ) {
            Date groupedDate = r.getGroupedDate();
            if( groupedDate.equals( parser.parse("2016-10-11 00:00") ) ) {
                assertEquals(1, r.getCount().longValue());
            } else if( groupedDate.equals( parser.parse("2016-10-12 00:00") ) ) {
                assertEquals(4, r.getCount().longValue());
            } else if( groupedDate.equals( parser.parse("2016-10-13 00:00") ) ) {
                assertEquals(3, r.getCount().longValue());
            } else if( groupedDate.equals( parser.parse("2016-10-14 00:00") ) ) {
                assertEquals(1, r.getCount().longValue());
            } else {
                fail("should go in one of those");
            }
        }



        List<CountByTimeModel<Long>> result2 =
                repo.reportByState(55, parser.parse("2016-10-11 00:00"), parser.parse("2016-10-15 00:00"));


        assertTrue(result2.isEmpty());
    }

    private StateOfOrderEntity createState(Date d, OrderStateEntity state, OrderEntity orderEntity) {
        StateOfOrderEntity en = new StateOfOrderEntity();
        en.setCreateDate( d );
        en.setState( state );
        en.setOrder(orderEntity);
        return en;
    }

    private StateOfOrderEntity createState(Date d, OrderStateEntity state) {
        return createState(d, state, null);
    }

}