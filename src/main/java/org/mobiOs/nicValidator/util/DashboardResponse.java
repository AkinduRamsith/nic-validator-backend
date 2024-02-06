package org.mobiOs.nicValidator.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardResponse {
    private Integer totalNIC;
    private Long totalMale;
    private Long totalFemale;
    private Long age1Male;
    private Long age1Female;
    private Long age2Male;
    private Long age2Female;
    private Long age3Male;
    private Long age3Female;
    private Long age4Male;
    private Long age4Female;
    private Long voters;
    private Long nonVoters;

}
