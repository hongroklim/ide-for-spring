package dev.rokong.dto;

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
    private String productNm;
    private String optionNm;

    public OrderProductDTO(int orderId){
        this.orderId = orderId;
    }

    public OrderProductDTO(int orderId, int productId, String optionCd){
        this(orderId);
        this.productId = productId;
        this.optionCd = optionCd;
    }
}