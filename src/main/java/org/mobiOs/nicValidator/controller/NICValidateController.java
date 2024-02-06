package org.mobiOs.nicValidator.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletResponse;
import org.mobiOs.nicValidator.dao.NICValidatorEntity;

import org.mobiOs.nicValidator.dto.NICValidatorDTO;
import org.mobiOs.nicValidator.service.NICValidatorService;
import org.mobiOs.nicValidator.util.DashboardResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/validator")
@CrossOrigin

public class NICValidateController {
    @Autowired
    private NICValidatorService nicValidatorService;


    @PostMapping(value = "/savenic", consumes = {"multipart/form-data"})
    public ResponseEntity<Set<NICValidatorDTO>> saveNIC(@RequestPart MultipartFile[] file) throws IOException {
        if(file !=null){
            Set<NICValidatorDTO> nicValidatorDTOS = nicValidatorService.saveNIC(file);
            if(null==nicValidatorDTOS){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(nicValidatorDTOS);
        }else{
            return ResponseEntity.notFound().build();
        }


    }

    @GetMapping("/getnic")
    public void exportAsCSV(HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String fileName="NIC-Details.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+ fileName + "");
        StatefulBeanToCsv<NICValidatorEntity> writer =new StatefulBeanToCsvBuilder<NICValidatorEntity>(response.getWriter())
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        writer.write(nicValidatorService.findAll());



    }

    @GetMapping(path = "/getDashboardInfo")
    public ResponseEntity<?> getDashBoardDetails(){
        DashboardResponse dashboardResponse=nicValidatorService.getDashBoardDetails();
        return ResponseEntity.ok(dashboardResponse);
    }

    @GetMapping("/getNicAsPdf")
    public void exportAsPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime=dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerKey,headerValue);

        nicValidatorService.exportAsPDF(response);

    }

    @GetMapping("/getNicAsExcel")
    public void exportAsExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime=dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=excel_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey,headerValue);
        nicValidatorService.exportAsExcel(response);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<Set<NICValidatorDTO>> getAllDetails(){
        Set<NICValidatorDTO> allDetails=nicValidatorService.getAllDetails();
        if(allDetails.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allDetails);
    }


}
