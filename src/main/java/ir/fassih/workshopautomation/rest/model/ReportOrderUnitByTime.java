package ir.fassih.workshopautomation.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportOrderUnitByTime {

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

}
