package dev.rokong.dto;

import dev.rokong.annotation.OrderStatus;
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
    private Integer deliveryPrice;
    private Integer price;
    private OrderStatus orderStatus;
    private String ShipCd;

    //sellerNm not primary key

    public OrderDeliveryDTO(int orderId){
        this.orderId = orderId;
    }

    public OrderDeliveryDTO(int orderId, int deliveryId){
        this(orderId);
        this.deliveryId = deliveryId;
    }

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

    public Integer getStatusCd(){
        if(this.orderStatus == null){
            return null;
        }else{
            return this.orderStatus.getCode();
        }
    }

}