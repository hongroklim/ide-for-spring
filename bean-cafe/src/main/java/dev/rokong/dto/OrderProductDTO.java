package dev.rokong.dto;

import dev.rokong.annotation.OrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Order Product", description="products included in order")
public class OrderProductDTO {

    @ApiModelProperty(value="order id", example="1", position=1)
    private int orderId;

    @ApiModelProperty(value="product id", example="3", position=2)
    private Integer productId;

    @ApiModelProperty(value="option code in Product Detail", example="0201", position=3)
    private String optionCd;

    @ApiModelProperty(value="seller name", example="seller1", position=4)
    private String sellerNm;

    @ApiModelProperty(value="product count", example="1", position=5)
    private Integer cnt;

    @ApiModelProperty(value="product's price", example="8500", position=6)
    private Integer price;

    @ApiModelProperty(value="product's discount price", example="-800", position=7)
    private Integer discountPrice;

    @ApiModelProperty(value="order status", example="WRITING", position=8)
    private OrderStatus orderStatus;

    @ApiModelProperty(value="delivery id", example="2", position=9)
    private Integer deliveryId;

    @ApiModelProperty(value="product's full name", example="Africa Special Beans", position=10)
    private String productNm;

    @ApiModelProperty(value="option's full name", example="Volume : 250g / Package : for gift", position=11)
    private String optionNm;
    
    //if optionCd is equal to NULL_OPTION_CD, it means only product
    public static final String NULL_OPTION_CD = ProductOptionDTO.TITLE_ID;

    @ApiModelProperty(hidden = true)
    public void setStatusCd(Integer statusCd){
        if(statusCd == null){
            return;
        }
    
        for(OrderStatus o : OrderStatus.values()){
            if(statusCd == o.getCode()){
                this.orderStatus = o;
                return;
            }
        }
    }

    @ApiModelProperty(hidden = true)
    public Integer getStatusCd(){
        if(this.orderStatus == null){
            return null;
        }else{
            return this.orderStatus.getCode();
        }
    }

    public OrderProductDTO(int orderId){
        this.orderId = orderId;
    }

    public OrderProductDTO(int orderId, int productId){
        this(orderId);
        this.productId = productId;
        this.optionCd = ProductOptionDTO.TITLE_ID.toString();
    }

    public OrderProductDTO(int orderId, int productId, String optionCd){
        this(orderId, productId);
        this.optionCd = optionCd;
    }
}