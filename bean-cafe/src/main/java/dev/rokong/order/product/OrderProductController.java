package dev.rokong.order.product;

import java.util.List;

import dev.rokong.util.ObjUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dev.rokong.dto.OrderProductDTO;

@RestController
@RequestMapping(value= "/order/{orderId}/product", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags={"Order Product"})
public class OrderProductController {

    @Autowired
    private OrderProductService oProductService;

    @RequestMapping(value="", method=RequestMethod.GET)
    @ApiOperation(value="get order products", notes="get products by order id [orderId]")
    public List<OrderProductDTO> getOProducts(@PathVariable int orderId){
        OrderProductDTO oProduct = new OrderProductDTO(orderId);
        return oProductService.getOProducts(oProduct);
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    @ApiOperation(value="add order product",
            notes="add product in order [orderId, productId, (optionCd), cnt, (payId)]")
    public OrderProductDTO addOProduct(@PathVariable int orderId, @RequestBody OrderProductDTO oProduct){
        oProduct.setOrderId(orderId);
        return oProductService.addOProduct(oProduct);
    }

    @RequestMapping(value= "/{productId}/{optionCd}", method=RequestMethod.GET)
    @ApiOperation(value="get order product", notes="get order product [orderId, productId, (optionCd)]")
    public OrderProductDTO getOProduct(@PathVariable int orderId, @PathVariable int productId,
                                       @PathVariable(required=false) String optionCd){
        OrderProductDTO oProduct = new OrderProductDTO(orderId, productId, optionCd);
        return oProductService.getOProduct(oProduct);
    }

    @RequestMapping(value="/{productId}/{optionCd}", method=RequestMethod.PUT)
    @ApiOperation(value="update count",
            notes="update order product's count [orderId, productId, (optionCd), cnt]")
    public OrderProductDTO updateOProduct(@PathVariable int orderId, @PathVariable int productId,
                                          @PathVariable(required=false) String optionCd,
                                          @RequestBody OrderProductDTO oProduct){
        oProduct.setOrderId(orderId);
        oProduct.setProductId(productId);
        oProduct.setOptionCd(optionCd);
        return oProductService.updateOProductCnt(oProduct);
    }

    @RequestMapping(value="/{productId}/{optionCd}", method=RequestMethod.DELETE)
    @ApiOperation(value="delete order product", notes="delete order product [orderId, productId, (optionCd)]")
    public void deleteOProduct(@PathVariable int orderId, @PathVariable int productId,
                               @PathVariable(required=false) String optionCd){
        OrderProductDTO oProduct = new OrderProductDTO(orderId, productId, optionCd);
        oProductService.deleteOProduct(oProduct);
    }
}
