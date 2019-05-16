package ir.fassih.workshopautomation.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportOrderUnitByTime {


    public enum DateRange {
        DAY, MONTH
    }

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    private Map<String, String> filter;

    private DateRange range;


}
