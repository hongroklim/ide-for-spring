package com.company.dto;

import com.company.annotation.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDTO {
    private int id;
    private String userNm;
    private Integer price;
    private Integer deliveryPrice;
    private Integer payId;
    private String payNm;
    private String requestDt;
    private OrderStatus orderStatus;
    private String lastEditDt;
    private String editorNm;

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

    public OrderDTO(int id){
        this.id = id;
    }
}