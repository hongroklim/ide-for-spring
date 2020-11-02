package dev.rokong.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private int id;
    private String name;
    private int price;
    private int categoryId;
    private Boolean enabled;
    private String sellerNm;
    private Integer stockCnt;
    private int deliveryPrice;
    private int discountPrice;

    public int getStockCntInt(){
        return (this.stockCnt != null) ? this.stockCnt : 0;
    }

    public boolean getEnabledBool(){
        return (this.enabled != null) ? this.enabled : false;
    }
}