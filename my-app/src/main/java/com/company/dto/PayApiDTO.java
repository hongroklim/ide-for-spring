package dev.rokong.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Pay Api", description="payment API information about order")
public class PayApiDTO {

    @ApiModelProperty(value="order id", example="1", position=1)
    private int orderId;

    @ApiModelProperty(value="api key", example="qwerdsf91123kjasdfs", position=2)
    private String apiKey;

    @ApiModelProperty(value="api name", example="TOSS", position=3)
    private String apiName;
}