package ir.fassih.workshopautomation.rest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface ExcelGenerator {

    default String generateExcelFileName(String prefix) {
        return prefix + "_" + new SimpleDateFormat("yyyy-mm-dd").format( new Date() ) + ".xls";
    }

    default void setHeadersOnRequest(HttpServletResponse response, String fileName) {
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
    }

    default List<String> getXslHeaders() {
        return new ArrayList<>();
    }

    default void generateExcelFile(String entityName, List<List<String>> data, HttpServletResponse response) throws IOException {
        setHeadersOnRequest(response, generateExcelFileName(entityName));
        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet(entityName);
        sheet.setDefaultColumnWidth(30);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFont(font);

        List<String> xslHeaders = getXslHeaders();
        Row header = sheet.createRow(0);
        for(int j = 0; j < xslHeaders.size(); j++) {
            header.createCell(j).setCellValue(xslHeaders.get(j));
            header.getCell(j).setCellStyle(style);
        }

        for (int i = 1; i <= data.size(); i ++) {
            Row row = sheet.createRow(i);
            List<String> rowData = data.get(i-1);
            for(int j = 0; j < rowData.size(); j++) {
                row.createCell(j).setCellValue(rowData.get(j));
                row.getCell(j).setCellStyle(style);
            }
        }

        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
    }
}
