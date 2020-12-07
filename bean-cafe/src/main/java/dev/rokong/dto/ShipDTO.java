package dev.rokong.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShipDTO {
    private int orderId;
    private String sellerNm;
    private Integer price;
    private Integer deliveryPrice;
    private String statusCd;

    public ShipDTO(int orderId){
        this.orderId = orderId;
    }

    public ShipDTO(int orderId, String sellerNm){
        this(orderId);
        this.sellerNm = sellerNm;
    }
}