package ir.fassih.workshopautomation.rest.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportByStateModel {

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    @NotNull
    private Long state;

    private Long user;


}
