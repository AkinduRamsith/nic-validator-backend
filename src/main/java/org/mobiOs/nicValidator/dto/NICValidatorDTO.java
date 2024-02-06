package org.mobiOs.nicValidator.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class NICValidatorDTO {
    private Long id;
    private String nic;
    private String gender;
    private Integer age;
    private LocalDate birthday;
    private Boolean isVoter;
}
