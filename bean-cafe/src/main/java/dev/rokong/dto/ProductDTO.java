package dev.rokong.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Product", description="Product including primary information")
public class ProductDTO {

    @ApiModelProperty(value="product id", example="2", position=1)
    private int id;

    @ApiModelProperty(value="product name", example="Bean Cafe Main Blending", position=2)
    private String name;

    @ApiModelProperty(value="product price", example="8000", position=3)
    private Integer price;

    @ApiModelProperty(value="product's category", example="3", position=4)
    private Integer categoryId;

    @ApiModelProperty(value="is enabled", example="true", position=5)
    private Boolean enabled;

    @ApiModelProperty(value="seller name", example="seller1", position=6)
    private String sellerNm;

    @ApiModelProperty(value="stock amount (can be empty)", example="", position=7)
    private Integer stockCnt;

    @ApiModelProperty(value="delivery id", example="2", position=8)
    private Integer deliveryId;

    @ApiModelProperty(value="delivery price", example="3000", position=9)
    private Integer deliveryPrice;

    @ApiModelProperty(value="discount price", example="-800", position=10)
    private Integer discountPrice;

    public ProductDTO(int id){
        this.id = id;
    }

    public int getStockCntInt(){
        return (this.stockCnt != null) ? this.stockCnt : 0;
    }
}