package ir.fassih.workshopautomation.repository.report;

import lombok.Getter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
public abstract  class AbstractReportModel<T> {

    private static SimpleDateFormat PARSER_YMD = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat PARSER_YM  = new SimpleDateFormat("yyyy-MM");


    private final Date date;
    private final T    data;


    AbstractReportModel(int year, int month, int day, T data) {
        String pattern = year + "-" + month + "-" + day;
        try {
            this.date = PARSER_YMD.parse(pattern);
            this.data = data;
        } catch (ParseException e) {
            throw new IllegalStateException("cannot parse " + pattern + " as yyyy-MM-dd", e);
        }
    }


    AbstractReportModel(int year, int month, T data) {
        String pattern = year + "-" + month;
        try {
            this.date = PARSER_YM.parse(pattern);
            this.data = data;
        } catch (ParseException e) {
            throw new IllegalStateException("cannot parse " + pattern + " as yyyy-MM", e);
        }
    }


    public static class DoubleReportModel extends AbstractReportModel<Double> {

        public DoubleReportModel(int year, int month, int day, Double data) {
            super(year, month, day, data);
        }


        public DoubleReportModel(int year, int month, Double data) {
            super(year, month, data);
        }
    }
}
