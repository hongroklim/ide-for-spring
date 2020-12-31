package dev.rokong.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewDTO {
    private int id;
    private String userNm;
    private int orderId;
    private int productId;
    private String optionCd;
    private Double rate;
    private String content;
    private Boolean isVisible;
    private String updateDt;

    public ReviewDTO(int id){
        this.id = id;
    }
}
