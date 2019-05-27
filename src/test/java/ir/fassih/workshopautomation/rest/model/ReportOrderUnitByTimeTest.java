package ir.fassih.workshopautomation.rest.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ReportOrderUnitByTimeTest {


    @Test
    public void test_deserialize() throws IOException {

        String jsonStr = "{ \"GE:createDate\":\"2019-1-1 00:00\" }";

        ReportOrderUnitByTime model = new ObjectMapper().readValue(jsonStr, ReportOrderUnitByTime.class);

        assertEquals("2019-1-1 00:00", model.getFilters().get("GE:createDate"));
    }

}