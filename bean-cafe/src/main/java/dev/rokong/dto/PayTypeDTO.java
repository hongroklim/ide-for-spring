package dev.rokong.dto;

import dev.rokong.annotation.PayType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Pay Type", description="Payment Methods")
public class PayTypeDTO {

    @ApiModelProperty(value="pay type id", example="1", position=1)
    private int id;

    @ApiModelProperty(value="pay type", example="TRANSFER", position=2)
    private PayType payType;

    @ApiModelProperty(value="payment option", example="국민은행", position=3)
    private String option1;

    @ApiModelProperty(value="sub option", example="", position=4)
    private String option2;

    @ApiModelProperty(value="is enabled", example="true", position=5)
    private Boolean enabled;

    public void setType(String type){
        this.payType = PayType.valueOf(type);
    }

    public String getType(){
        return (this.payType == null) ? null : this.payType.name();
    }

    public PayTypeDTO(int id){
        this.id = id;
    }

}
