package dev.rokong.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Delivery", description="destination of order")
public class DeliveryDTO {

    @ApiModelProperty(value="order id", example="1", position=1)
    private int orderId;

    @ApiModelProperty(value="user name", example="customer1", position=2)
    private String userNm;

    @ApiModelProperty(value="sender name", example="발송자", position=3)
    private String senderNm;

    @ApiModelProperty(value="recipient name", example="수신자", position=4)
    private String recipientNm;

    @ApiModelProperty(value="zipCode", example="32800", position=5)
    private Integer zipCd;

    @ApiModelProperty(value="main address", example="충청남도 계룡시 계룡대로 663", position=6)
    private String address1;

    @ApiModelProperty(value="sub address", example="사서함 501-329", position=7)
    private String address2;

    @ApiModelProperty(value="primary contact", example="010-0000-1111", position=8)
    private String contact1;

    @ApiModelProperty(value="secondary contact", example="042-552-3668", position=9)
    private String contact2;

    @ApiModelProperty(value="message for delivery", example="부재시 문앞에", position=10)
    private String message;

    public DeliveryDTO(int orderId){
        this.orderId = orderId;
    }

    public DeliveryDTO(int orderId, String userNm){
        this(orderId);
        this.userNm = userNm;
    }
}