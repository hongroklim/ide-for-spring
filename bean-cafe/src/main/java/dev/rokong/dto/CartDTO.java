package dev.rokong.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartDTO {
    private String userNm;
    private Integer productId;
    private String optionCd;
    private Integer cnt;
    private String updateDt;

    public CartDTO(String userNm){
        this.userNm = userNm;
    }

    public CartDTO(String userNm, Integer productId, String optionCd){
        this(userNm);
        this.productId = productId;
        this.optionCd = optionCd;
    }

    public CartDTO(CartDTO cart){
        this(cart.getUserNm(), cart.getProductId(), cart.getOptionCd());
    }
}