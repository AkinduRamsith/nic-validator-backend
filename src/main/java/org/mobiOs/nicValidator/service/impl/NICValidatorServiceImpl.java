package org.mobiOs.nicValidator.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.mobiOs.nicValidator.dao.NICValidatorEntity;
import org.mobiOs.nicValidator.dto.NICCsvRepresentation;
import org.mobiOs.nicValidator.dto.NICValidatorDTO;
import org.mobiOs.nicValidator.repository.NICValidatorRepository;
import org.mobiOs.nicValidator.service.NICValidatorService;
import org.mobiOs.nicValidator.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NICValidatorServiceImpl implements NICValidatorService {

    private final NICValidatorRepository nicValidatorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public NICValidatorServiceImpl(NICValidatorRepository nicValidatorRepository) {
        this.nicValidatorRepository = nicValidatorRepository;
    }

    @Override
    public Set<NICValidatorDTO> saveNIC(MultipartFile[] file) throws IOException {

        Set<NICValidatorEntity> nicValidatorEntities = parseCSV(file);
        Set<NICValidatorDTO> nicValidatorDTOS=nicValidatorEntities.stream().map(entity -> modelMapper.map(entity,NICValidatorDTO.class))
                        .collect(Collectors.toSet());

        nicValidatorRepository.saveAll(nicValidatorEntities);
        return nicValidatorEntities !=null ? nicValidatorDTOS : null;
    }

    @Override
    public List<NICValidatorEntity> findAll() {
        log.info(String.valueOf(nicValidatorRepository.findAll()));
       return nicValidatorRepository.findAll();
    }

    @Override
    public void exportAsPDF(HttpServletResponse response) throws IOException {

        List<NICValidatorEntity> all = nicValidatorRepository.findAll();
        Document document = new Document(PageSize.A4);
        try{
            PdfWriter.getInstance(document,response.getOutputStream());

            document.open();
            Font fontHeader = FontFactory.getFont(FontFactory.TIMES_BOLD);
            fontHeader.setSize(22);

            Paragraph headerParagraph = new Paragraph("NIC Validator Report",fontHeader);
            headerParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            headerParagraph.setSpacingAfter(20);

            Font fontContent = FontFactory.getFont(FontFactory.TIMES);
            fontContent.setSize(12);

            float[] columnWidth = {3,5,4,2,3};
            boolean fixedWidth=true;
            Table tbl = new Table(5);
            tbl.setWidths(columnWidth);
            String[] headers={"ID", "NIC" , "Gender" , "Age" , "BirthDay"};
            for(String header : headers){
                Cell headerCell = new Cell(new Phrase(header,fontContent));
                headerCell.setBackgroundColor(Color.lightGray);
                headerCell.setHorizontalAlignment(Cell.ALIGN_CENTER);
                headerCell.setVerticalAlignment(Cell.ALIGN_MIDDLE);


                headerCell.setLeading(12);


                tbl.addCell(headerCell);
            }

            for(NICValidatorEntity nicEntiy1 : all){

                tbl.addCell(createStyleCell(String.valueOf(nicEntiy1.getId()),fontContent));
                tbl.addCell(createStyleCell(String.valueOf(nicEntiy1.getNic()),fontContent));
                tbl.addCell(createStyleCell(String.valueOf(nicEntiy1.getGender()),fontContent));
                tbl.addCell(createStyleCell(String.valueOf(nicEntiy1.getAge()),fontContent));
                tbl.addCell(createStyleCell(String.valueOf(nicEntiy1.getBirthday()),fontContent));

            }
            Paragraph pdfParagraph=new Paragraph();
            pdfParagraph.add(tbl);
            pdfParagraph.setSpacingAfter(20);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
            Paragraph metaDataParagraph = new Paragraph("Report generated on : " +dateFormat.format(new Date()),fontContent );
            metaDataParagraph.setAlignment(Paragraph.ALIGN_RIGHT);



            document.add(headerParagraph);
            document.add(pdfParagraph);
            document.add(metaDataParagraph);
        }catch (DocumentException e){
            e.printStackTrace();
        }finally {
            document.close();
        }


    }

    @Override
    public void exportAsExcel(HttpServletResponse response) throws IOException {
        new ExcelExportUtils(nicValidatorRepository.findAll()).exportAsExcel(response);
    }

    @Override
    public Set<NICValidatorDTO> getAllDetails() {
        List<NICValidatorEntity> allEntities = nicValidatorRepository.findAll();
        return allEntities.stream().map(entity-> modelMapper.map(entity,NICValidatorDTO.class)).collect(Collectors.toSet());
    }

    @Override
    public DashboardResponse getDashBoardDetails() {
        List<NICValidatorEntity> all = nicValidatorRepository.findAll();
        int size=all.size();

        long maleCount=all.stream().filter(entity -> "Male".equalsIgnoreCase(entity.getGender())).count();
        long femaleCount=all.stream().filter(entity -> "Female".equalsIgnoreCase(entity.getGender())).count();

//                    age Validator
        long age18to25CountMale=all.stream().filter(entity->"Male".equalsIgnoreCase(entity.getGender()) && entity.getAge() >=18 && entity.getAge() <=25).count();
        long age25to45CountMale=all.stream().filter(entity->"Male".equalsIgnoreCase(entity.getGender()) && entity.getAge() >25 && entity.getAge() <=45).count();
        long age45to65CountMale=all.stream().filter(entity->"Male".equalsIgnoreCase(entity.getGender()) && entity.getAge() >=45 && entity.getAge() <=65).count();
        long age65PlusCountMale=all.stream().filter(entity->"Male".equalsIgnoreCase(entity.getGender()) && entity.getAge() > 65).count();

        long age18to25CountFemale=all.stream().filter(entity->"Female".equalsIgnoreCase(entity.getGender()) && entity.getAge() >=18 && entity.getAge() <=25).count();
        long age25to45CountFemale=all.stream().filter(entity->"Female".equalsIgnoreCase(entity.getGender()) && entity.getAge() >25 && entity.getAge() <=45).count();
        long age45to65CountFemale=all.stream().filter(entity->"Female".equalsIgnoreCase(entity.getGender()) && entity.getAge() >=45 && entity.getAge() <=65).count();
        long age65PlusCountFemale=all.stream().filter(entity->"Female".equalsIgnoreCase(entity.getGender()) && entity.getAge() > 65).count();

        long voters=all.stream().filter(entity-> entity.getIsVoter()).count();
        long nonVoters=all.stream().filter(entity-> !entity.getIsVoter()).count();


        return new DashboardResponse(size,maleCount,femaleCount,age18to25CountMale,age18to25CountFemale,age25to45CountMale,age25to45CountFemale,age45to65CountMale,age45to65CountFemale,age65PlusCountMale,age65PlusCountFemale,voters,nonVoters);
    }

    private Cell createStyleCell(String text, Font font) {
        Cell cell =new Cell(new Phrase(text,font));
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
        cell.setLeading(12);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private Set<NICValidatorEntity> parseCSV(MultipartFile[] files) throws IOException {
        Set<NICValidatorEntity> nicValidatorEntities = new HashSet<>();
        for (MultipartFile file : files) {
//
                try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                    HeaderColumnNameMappingStrategy<NICCsvRepresentation> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
                    mappingStrategy.setType(NICCsvRepresentation.class);
                    CsvToBean<NICCsvRepresentation> csvToBean = new CsvToBeanBuilder<NICCsvRepresentation>(reader)
                            .withMappingStrategy(mappingStrategy)
                            .withIgnoreEmptyLine(true)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();

                    Set<NICValidatorDTO> collect = csvToBean.parse().stream().map(csvLine -> NICValidatorDTO.builder()
                            .nic(csvLine.getNicNumber()).build()
                    ).collect(Collectors.toSet());
                    for (NICValidatorDTO nicValidatorDTO : collect) {
                        if (validateNIC(nicValidatorDTO.getNic())) {
                            List<NICValidatorDTO> validateList = generateNICValidator(nicValidatorDTO.getNic());
                            nicValidatorEntities.addAll(validateList.stream()
                                    .map(entityDTO ->
                                            NICValidatorEntity.builder()
                                                    .nic(entityDTO.getNic())
                                                    .gender(entityDTO.getGender())
                                                    .age(entityDTO.getAge())
                                                    .birthday(entityDTO.getBirthday())
                                                    .isVoter(entityDTO.getIsVoter())
                                                    .build()
                                    ).collect(Collectors.toSet())
                            );
                        }
                    }
                }



        }
        return nicValidatorEntities;
    }

    private boolean validateNIC(String nic) {
        return (nic.length() == 10 &&
                nic.substring(0, 9).matches("\\d+") &&
                !Character.isDigit(nic.charAt(9)) &&
                "xv".contains(String.valueOf(nic.charAt(9)).toLowerCase()) || nic.length() == 12 && nic.substring(0, 9).matches("\\d+"));
    }


    private List<NICValidatorDTO> generateNICValidator(String nic) {
        List<MonthData> monthDataList = new ArrayList<>();
        monthDataList.add(new MonthData("January", 31));
        monthDataList.add(new MonthData("February", 28));
        monthDataList.add(new MonthData("March", 31));
        monthDataList.add(new MonthData("April", 30));
        monthDataList.add(new MonthData("May", 31));
        monthDataList.add(new MonthData("June", 30));
        monthDataList.add(new MonthData("July", 31));
        monthDataList.add(new MonthData("August", 31));
        monthDataList.add(new MonthData("September", 30));
        monthDataList.add(new MonthData("October", 31));
        monthDataList.add(new MonthData("November", 30));
        monthDataList.add(new MonthData("December", 31));

        LocalDate currentDate = LocalDate.now();
        List<NICValidatorDTO> result = new ArrayList<>();
        ExtractData extractData = extractedData(nic);
        Integer dayList = extractData != null ? extractData.getDayList() : null;
        DayAndGender dayAndGender = findDayAndGender(dayList, monthDataList);
        if (nic.length() == 10) {
            try{
                LocalDate bday = LocalDate.of((extractData != null ? extractData.getYear() : null) + 1900, Month.valueOf(dayAndGender.getMonth().toUpperCase()), dayAndGender.getDay());
                Integer age = Period.between(bday, currentDate).getYears();
                Boolean isVoter=false;
                if(nic.substring(9,10).equalsIgnoreCase("v")){
                    isVoter=true;
                }else if(nic.substring(9,10).equalsIgnoreCase("x")){
                    isVoter=false;
                }
                result.add(
                        NICValidatorDTO.builder().
                                nic(nic).
                                gender(dayAndGender.getGender()).
                                age(age).
                                birthday(bday).
                                isVoter(isVoter) .build()

                );
            }catch (DateTimeException e){
                if("February".equalsIgnoreCase(dayAndGender.getMonth()) && dayAndGender.getDay()==29){
                    monthDataList.get(1).setDays(29);
                    return generateNICValidator(nic);
                }
            }

        } else if (nic.length() == 12) {
            try{
                LocalDate bday = LocalDate.of(extractData != null ? extractData.getYear() : null, Month.valueOf(dayAndGender.getMonth().toUpperCase()), dayAndGender.getDay());
                Integer age = Period.between(bday, currentDate).getYears();
                Boolean isVoter=false;
                if(age>19){
                    isVoter=true;
                }else if(age<19){
                    isVoter=false;
                }
                result.add(
                        NICValidatorDTO.builder().
                                nic(nic).
                                gender(dayAndGender.getGender()).
                                age(age).
                                birthday(bday).
                                isVoter(isVoter) .build()

                );
            }catch (DateTimeException e){
                if("February".equalsIgnoreCase(dayAndGender.getMonth()) && dayAndGender.getDay()==29){
                    monthDataList.get(1).setDays(29);
                    return generateNICValidator(nic);
                }
            }

        }

        return result;
    }

    private DayAndGender findDayAndGender(Integer dayList, List<MonthData> monthDataList) {
        DayAndGender dayAndGender = new DayAndGender();
        if (dayList < 500) {
            dayAndGender.setGender("Male");
        } else {
            dayAndGender.setGender("Female");
            dayList = dayList - 500;
        }

        for (MonthData monthData : monthDataList) {
            if (monthData.getDays() < dayList) {
                dayList = dayList - monthData.getDays();
            } else {
                dayAndGender.setDay(dayList);
                dayAndGender.setMonth(monthData.getMonth());
                break;
            }
        }
        return dayAndGender;
    }

    private ExtractData extractedData(String nic) {
        if (nic.length() == 10) {

            return new ExtractData(Integer.valueOf(nic.substring(0, 2)), Integer.valueOf(nic.substring(2, 5)), nic.substring(9, 10));
        } else if (nic.length() == 12) {

            return new ExtractData(Integer.valueOf(nic.substring(0, 4)), Integer.valueOf(nic.substring(4, 7)), null);
        }
        return null;
    }


}
