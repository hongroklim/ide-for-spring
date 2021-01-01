package dev.rokong.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Product Detail", description="detail options of product")
public class ProductDetailDTO {

    @ApiModelProperty(value="product id", example="3", position=1)
    private int productId;

    @ApiModelProperty(value="option code", example="0201", position=2)
    private String optionCd;

    @ApiModelProperty(value="option's full name", example="Volume : 250g / Package : for gift", position=3)
    private String fullNm;

    @ApiModelProperty(value="change price from original", example="500", position=4)
    private Integer priceChange;

    @ApiModelProperty(value="stock amount", example="3", position=5)
    private Integer stockCnt;

    @ApiModelProperty(value="is enabled", example="true", position=6)
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