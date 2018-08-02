package ir.fassih.workshopautomation.manager;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import ir.fassih.workshopautomation.core.datamanagment.model.SearchModel;
import ir.fassih.workshopautomation.manager.test.SampleEntity;
import ir.fassih.workshopautomation.manager.test.SampleManager;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AbstractManagerTest {

    @Autowired
    SampleManager manager;

    @Test
    public void searchTestGtLong() {
        List<SampleEntity> entities = new ArrayList<>();
        entities.add(SampleEntity.builder().amount(100L).username("user 1").build());
        entities.add(SampleEntity.builder().amount(120L).username("user 2").build());
        entities.add(SampleEntity.builder().amount(130L).username("user 3").build());
        entities.add(SampleEntity.builder().amount(140L).username("user 4").build());
        entities.add(SampleEntity.builder().amount(150L).username("user 5").build());
        manager.saveAll(entities);
        Map<String, String> filters = new HashMap<>();
        filters.put("EQ:username", "user 2");
        SearchModel model = SearchModel.builder().filters(filters).build();
        Page<SampleEntity> search = manager.search(model);
        assertEquals(1, search.getContent().size());
    }


    @Test
    public void searchTestGtDate() {
        List<SampleEntity> entities = new ArrayList<>();
        entities.add(SampleEntity.builder().creationTime(new Date(1534717067608L)).username("user 1").build());
        entities.add(SampleEntity.builder().creationTime(new Date(1532717069608L)).amount(120L).username("user 2").build());
        entities.add(SampleEntity.builder().amount(130L).username("user 3").creationTime(new Date(1542717169608L)).build());
        entities.add(SampleEntity.builder().amount(140L).username("user 4").creationTime(new Date(1542717169608L)).build());
        entities.add(SampleEntity.builder().amount(150L).username("user 5").creationTime(new Date(1542717169608L)).build());
        manager.saveAll(entities);
        Map<String, String> filters = new HashMap<>();
        filters.put("EQ:creationTime", "1532717069608");
        SearchModel model = SearchModel.builder()
                .filters(filters)
                .build();

        Page<SampleEntity> search = manager.search(model);
        assertEquals(1, search.getContent().size());
    }

}
