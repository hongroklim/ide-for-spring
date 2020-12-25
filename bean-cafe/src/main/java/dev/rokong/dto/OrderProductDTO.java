package dev.rokong.dto;

import dev.rokong.annotation.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderProductDTO {
    private int orderId;
    private Integer productId;
    private String optionCd;
    private String sellerNm;
    private Integer cnt;
    private Integer price;
    private Integer discountPrice;
    private OrderStatus orderStatus;
    private Integer deliveryId;
    private String productNm;
    private String optionNm;
    private Boolean isValid;
    
    //if optionCd is equal to NULL_OPTION_CD, it means only product
    public static final String NULL_OPTION_CD = ProductOptionDTO.TITLE_ID;

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

    public OrderProductDTO(int orderId){
        this.orderId = orderId;
    }

    public OrderProductDTO(int orderId, int productId){
        this(orderId);
        this.productId = productId;
        this.optionCd = ProductOptionDTO.TITLE_ID.toString();
    }

    public OrderProductDTO(int orderId, int productId, String optionCd){
        this(orderId, productId);
        this.optionCd = optionCd;
    }
}