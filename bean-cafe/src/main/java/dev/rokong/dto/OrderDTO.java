package dev.rokong.dto;

import dev.rokong.annotation.OrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Order", description="Order including primary information")
public class OrderDTO {

    @ApiModelProperty(value="order id", example="1", position=1)
    private int id;

    @ApiModelProperty(value="customer name", example="customer1", position=2)
    private String userNm;

    @ApiModelProperty(value="products' price", example="13700", position=3)
    private Integer price;

    @ApiModelProperty(value="delivery price", example="5500", position=4)
    private Integer deliveryPrice;

    @ApiModelProperty(value="pay id", example="7", position=5)
    private Integer payId;

    @ApiModelProperty(value="pay name", example="", position=6)
    private String payNm;

    @ApiModelProperty(value="order request date", example="2020-10-18", position=7)
    private String requestDt;

    @ApiModelProperty(value="last changed date", example="2020-10-18", position=8)
    private String lastEditDt;

    @ApiModelProperty(value="order status", example="WRITING", position=9)
    private OrderStatus orderStatus;

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

    public OrderDTO(int id){
        this.id = id;
    }
}