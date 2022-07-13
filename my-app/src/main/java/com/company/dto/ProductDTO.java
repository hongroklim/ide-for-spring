package com.company.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private int id;
    private String name;
    private Integer price;
    private Integer categoryId;
    private Boolean enabled;
    private String sellerNm;
    private Integer stockCnt;
    private Integer deliveryId;
    private Integer deliveryPrice;
    private Integer discountPrice;

    public int getStockCntInt(){
        return (this.stockCnt != null) ? this.stockCnt : 0;
    }
}