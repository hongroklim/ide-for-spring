package dev.rokong.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Review", description="Review from purchased products")
public class ReviewDTO {

    @ApiModelProperty(value="review id", example="1", position=1)
    private int id;

    @ApiModelProperty(value="writer name", example="customer1", position=2)
    private String userNm;

    @ApiModelProperty(value="order id", example="2", position=3)
    private int orderId;

    @ApiModelProperty(value="product id", example="3", position=4)
    private int productId;

    @ApiModelProperty(value="option code", example="0101", position=5)
    private String optionCd;

    @ApiModelProperty(value="rate", example="7.2", position=6)
    private Double rate;

    @ApiModelProperty(value="content", example="not bad.", position=7)
    private String content;

    @ApiModelProperty(value="is visible", example="true", position=8)
    private Boolean isVisible;

    @ApiModelProperty(value="update date", example="2020-12-31", position=9)
    private String updateDt;

    public ReviewDTO(int id){
        this.id = id;
    }
}
