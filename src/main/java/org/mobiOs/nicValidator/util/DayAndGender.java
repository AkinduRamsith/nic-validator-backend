package org.mobiOs.nicValidator.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DayAndGender {
    private Integer day;
    private String month;
    private String gender;
}
