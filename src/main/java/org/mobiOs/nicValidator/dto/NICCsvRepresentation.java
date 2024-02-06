package org.mobiOs.nicValidator.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NICCsvRepresentation {

    @CsvBindByName(column = "nicNumber")
    private String nicNumber;
}
