package dev.rokong.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PayTypeDTO {
    private int id;
    private String type;
    private String option1;
    private String option2;
    private Boolean enabled;

}
