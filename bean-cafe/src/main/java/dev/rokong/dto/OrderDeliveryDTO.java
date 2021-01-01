package dev.rokong.dto;

import dev.rokong.annotation.OrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Order Delivery", description="in order, independent delivery classified by product delivery")
public class OrderDeliveryDTO {

    @ApiModelProperty(value="order id", example="1", position=1)
    private int orderId;

    @ApiModelProperty(value="delivery id", example="2", position=2)
    private int deliveryId;

    @ApiModelProperty(value="seller name", example="seller1", position=3)
    private String sellerNm;

    @ApiModelProperty(value="delivery type name", example="DOMESTIC", position=4)
    private String typeNm;

    @ApiModelProperty(value="delivery name", example="", position=5)
    private String deliveryNm;

    @ApiModelProperty(value="delivery price", example="3000", allowableValues="range[0, infinity]", position=6)
    private Integer deliveryPrice;

    @ApiModelProperty(value="products' price in same delivery", example="7700", allowableValues="range[0, infinity]", position=7)
    private Integer price;

    @ApiModelProperty(value="order status", example="WRITING", position=8)
    private OrderStatus orderStatus;

    @ApiModelProperty(value="shipment code", example="", position=9)
    private String ShipCd;

    public OrderDeliveryDTO(int orderId){
        this.orderId = orderId;
    }

    public OrderDeliveryDTO(int orderId, int deliveryId){
        this(orderId);
        this.deliveryId = deliveryId;
    }

    @ApiModelProperty(hidden = true)
    public void setStatusCd(Integer statusCd){
        if(statusCd == null){
            return;
        }

        for(OrderStatus o : OrderStatus.values()){
            if(statusCd == o.getCode()){
                this.orderStatus = o;
                return;
            }
        }
    }

    @ApiModelProperty(hidden = true)
    public Integer getStatusCd(){
        if(this.orderStatus == null){
            return null;
        }else{
            return this.orderStatus.getCode();
        }
    }

}