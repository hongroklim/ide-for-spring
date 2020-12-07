package dev.rokong.dto;

import dev.rokong.annotation.DeliveryType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDeliveryDTO {
    private int id;
    private String sellerNm;
    private DeliveryType deliveryType;
    private String name;
    private Integer price;

    public String getType(){
        return (this.deliveryType == null) ? null : this.deliveryType.name();
    }

    public void setType(String type){
        this.deliveryType = DeliveryType.valueOf(DeliveryType.class, type);
    }
}