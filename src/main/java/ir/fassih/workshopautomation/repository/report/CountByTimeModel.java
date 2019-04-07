package ir.fassih.workshopautomation.repository.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface CountByTimeModel {

    SimpleDateFormat PARSER = new SimpleDateFormat("yyyy-MM-dd");

    @JsonIgnore
    int getY();

    @JsonIgnore
    int getM();

    @JsonIgnore
    int getD();


    Long getCount();


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    default Date getGroupedDate() {
        try {
            String d = getY() + "-" + getM() + "-" + getD();
            return PARSER.parse(d);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }
}
