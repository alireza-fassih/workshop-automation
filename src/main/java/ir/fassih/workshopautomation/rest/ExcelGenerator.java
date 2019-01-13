package ir.fassih.workshopautomation.rest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface ExcelGenerator {

    default String generateExcelFileName(String prefix) {
        return prefix + "_" + new SimpleDateFormat("yyyy-mm-dd").format( new Date() ) + ".csv";
    }

    default void setHeadersOnRequest(HttpServletResponse response, String fileName) {
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentType("text/csv; charset=UTF-8");
    }

    default List<String> getXslHeaders() {
        return new ArrayList<>();
    }

    default void generateExcelFile(String entityName, List<List<String>> data, HttpServletResponse response) throws IOException {
        setHeadersOnRequest(response, generateExcelFileName(entityName));
        try (PrintWriter p =  new PrintWriter(response.getOutputStream())) {
            p.print("\uFEFF");
            p.println(String.join(", ", getXslHeaders()));
            for (List<String> row: data){
                p.println(String.join(", ", row));
            }
            p.flush();
        }
    }
}
