package dev.rokong.dto;

import dev.rokong.annotation.OrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Pay Status", description="Payment Status")
public class PayStatusDTO {

    @ApiModelProperty(value="order id", example="1", position=1)
    private int orderId;

    @ApiModelProperty(value="pay id", example="7", position=2)
    private int payId;

    @ApiModelProperty(value="api name", example="TOSS", position=3)
    private String apiName;

    @ApiModelProperty(value="api key", example="qwerdsf91123kjasdfs", position=4)
    private String apiKey;

    @ApiModelProperty(value="order status", example="WRITING", position=5)
    private OrderStatus orderStatus;

    @ApiModelProperty(value="total price", example="19200", position=6)
    private int price;

    @ApiModelProperty(value="pay method", example="CARD", position=7)
    private String payMethod;
}
