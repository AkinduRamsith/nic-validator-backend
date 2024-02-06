package org.mobiOs.nicValidator.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExtractData {
    private Integer year;
    private Integer dayList;
    private String character;
}
