package com.company.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDetailDTO {
    private int productId;
    private String optionCd;
    private String fullNm;
    private Integer priceChange;
    private Integer stockCnt;
    private Boolean enabled;

    public ProductDetailDTO(int productId){
        this.productId = productId;
    }

    public ProductDetailDTO(int productId, String optionCd){
        this(productId);
        this.optionCd = optionCd;
    }

    public ProductDetailDTO(ProductDetailDTO pDetail){
        this(pDetail.getProductId(), pDetail.getOptionCd());
    }
    
    /**
     * get id in specified option group
     * 
     * @param optionGroup where id to find exists
     * @return id
     */
    public String idOfGroup(int optionGroup){
        if(this.optionCd == null || "".equals(optionCd)){
            throw new IllegalStateException("optionCd is not defined");
        }else if(this.optionCd.length()/2 < optionGroup){
            throw new IndexOutOfBoundsException(
                "optionGroup exceeds maximum : " +this.optionCd.length()/2);
        }else if(optionGroup == 0){
            throw new IllegalArgumentException("optionGroup is not defined");
        }

        return this.optionCd.substring((optionGroup-1)*2, ((optionGroup-1)*2)+2);
    }
}