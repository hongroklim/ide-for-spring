package dev.rokong.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductOptionDTO {
    private int productId;
    private Integer optionGroup;
    private String optionId;
    private String name;

    public ProductOptionDTO(int pId){
        this.productId = pId;
    }

    public ProductOptionDTO(int pId, int groupId){
        this.productId = pId;
        this.optionGroup = groupId;
    }

    public ProductOptionDTO(int pId, int groupId, String optionId){
        this.productId = pId;
        this.optionGroup = groupId;
        this.optionId = optionId;
    }

    public ProductOptionDTO(ProductOptionDTO pOption){
        this.productId = pOption.getProductId();
        this.optionGroup = pOption.getOptionGroup();
        this.optionId = pOption.getOptionId();
    }
}