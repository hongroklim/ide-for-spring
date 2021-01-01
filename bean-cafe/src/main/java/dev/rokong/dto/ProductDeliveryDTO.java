package dev.rokong.dto;

import dev.rokong.annotation.DeliveryType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Product Delivery", description="the way products are delivered")
public class ProductDeliveryDTO {

    @ApiModelProperty(value="product delivery id", example="2", position=1)
    private int id;

    @ApiModelProperty(value="seller name", example="seller1", position=2)
    private String sellerNm;

    @ApiModelProperty(value="delivery type", example="DOMESTIC", position=3)
    private DeliveryType deliveryType;

    @ApiModelProperty(value="delivery name", example="", position=4)
    private String name;

    @ApiModelProperty(value="delivery price", example="3000", position=5)
    private Integer price;

    public String getType(){
        return (this.deliveryType == null) ? null : this.deliveryType.name();
    }

    public void setType(String type){
        this.deliveryType = DeliveryType.valueOf(DeliveryType.class, type);
    }
}