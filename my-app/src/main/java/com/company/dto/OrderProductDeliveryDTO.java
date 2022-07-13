package com.company.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderProductDeliveryDTO {
    private int orderId;
    private int deliveryId;
    private String sellerNm;
    private String typeNm;
    private String deliveryNm;
    private Integer price;

    //sellerNm not primary key

    public OrderProductDeliveryDTO(int orderId){
        this.orderId = orderId;
    }

    public OrderProductDeliveryDTO(int orderId, int deliveryId){
        this(orderId);
        this.deliveryId = deliveryId;
    }

}