package com.company.dto;

import com.company.annotation.PayType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PayTypeDTO {
    
    private int id;
    private PayType payType;
    private String option1;
    private String option2;
    private Boolean enabled;

    public void setType(String type){
        PayType tobePayType = PayType.valueOf(type);
        this.payType = tobePayType;
    }

    public String getType(){
        return (this.payType == null) ? null : this.payType.name();
    }

    public PayTypeDTO(int id){
        this.id = id;
    }

}
