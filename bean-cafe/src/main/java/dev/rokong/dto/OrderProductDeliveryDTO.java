package dev.rokong.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderProductDeliveryDTO {
    private int orderId;
    private String sellerNm;
    private int deliveryId;
    private String typeNm;
    private String deliveryNm;
    private Integer price;

    //sellerNm not primary key

    public OrderProductDeliveryDTO(int orderId){
        this.orderId = orderId;
    }

    public OrderProductDeliveryDTO(int orderId, String sellerNm){
        this(orderId);
        this.sellerNm = sellerNm;
    }

    public OrderProductDeliveryDTO(int orderId, String sellerNm, int deliveryId){
        this(orderId, sellerNm);
        this.deliveryId = deliveryId;
    }
}