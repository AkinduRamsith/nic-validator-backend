package org.mobiOs.nicValidator.util;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mobiOs.nicValidator.dao.NICValidatorEntity;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelExportUtils {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<NICValidatorEntity> entityList;

    public ExcelExportUtils(List<NICValidatorEntity> entityList) {
        this.entityList = entityList;
        workbook =new XSSFWorkbook();
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style){
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if(value instanceof Integer){
            cell.setCellValue((Integer) value);
        }else  if(value instanceof Double){
            cell.setCellValue((Double) value);
        }else  if(value instanceof Boolean){
            cell.setCellValue((Boolean) value);
        }else  if(value instanceof Long) {
            cell.setCellValue((Long) value);
        }else if(value instanceof LocalDate){
            LocalDate dateValue= (LocalDate) value;
            DateTimeFormatter formatter =DateTimeFormatter.ofPattern("yyyy-MM-dd");
            cell.setCellValue(dateValue.format(formatter));
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void createHeaderRow(){
        sheet = workbook.createSheet("NIC Validate Report");
        Row row = sheet.createRow(0);
        CellStyle style= workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row,0,"NIC Validate Report",style);
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,5));
        font.setFontHeightInPoints((short) 10);

        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row,0,"ID",style);
        createCell(row,1,"NIC Number",style);
        createCell(row,2,"Gender",style);
        createCell(row,3,"Age",style);
        createCell(row,4,"Birthday",style);
    }

    private void writeNICValidateDetails(){
        int rowCount=2;
        CellStyle style=workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for(NICValidatorEntity nicValidator:entityList){
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row,columnCount++,nicValidator.getId(),style);
            createCell(row,columnCount++,nicValidator.getNic(),style);
            createCell(row,columnCount++,nicValidator.getGender(),style);
            createCell(row,columnCount++,nicValidator.getAge(),style);
            createCell(row,columnCount++,nicValidator.getBirthday(),style);
        }
    }

    public void exportAsExcel(HttpServletResponse response) throws IOException {
        createHeaderRow();
        writeNICValidateDetails();
        ServletOutputStream servletOutputStream=response.getOutputStream();
        workbook.write(servletOutputStream);
        workbook.close();
        servletOutputStream.close();
    }
}
