package dev.rokong.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Cart", description="Where users put products to purchase")
public class CartDTO {

    @ApiModelProperty(value="user name", example="customer1", position=1)
    private String userNm;

    @ApiModelProperty(value="product id", example="3", position=2)
    private Integer productId;

    @ApiModelProperty(value="option cd (empty when only product)", example="0201", position=3)
    private String optionCd;

    @ApiModelProperty(value="product's amount to purchase", example="2", allowableValues = "range[1, infinity]", position=4)
    private Integer cnt;

    @ApiModelProperty(value="the last update time", example="2020-10-18", position=5)
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