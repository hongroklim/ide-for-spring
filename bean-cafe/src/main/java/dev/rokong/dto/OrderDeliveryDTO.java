package dev.rokong.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDeliveryDTO {
    private int orderId;
    private int deliveryId;
    private String sellerNm;
    private String typeNm;
    private String deliveryNm;
    private Integer price;

    //sellerNm not primary key

    public OrderDeliveryDTO(int orderId){
        this.orderId = orderId;
    }

    public OrderDeliveryDTO(int orderId, int deliveryId){
        this(orderId);
        this.deliveryId = deliveryId;
    }

}