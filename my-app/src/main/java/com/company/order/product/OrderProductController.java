package com.company.order.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.company.dto.OrderProductDTO;

@RestController
@RequestMapping("/order/{oId}/product")
@ResponseStatus(HttpStatus.OK)
public class OrderProductController {

    @Autowired OrderProductService oProductService;

    @RequestMapping(value="", method=RequestMethod.GET)
    public List<OrderProductDTO> getOProducts(@PathVariable int oId){
        OrderProductDTO oProduct = new OrderProductDTO(oId);
        return oProductService.getOProducts(oProduct);
    }

    @RequestMapping(value="/{pId}/option/{option}", method=RequestMethod.GET)
    public OrderProductDTO getOProduct(@PathVariable int oId,
            @PathVariable int pId, @PathVariable String option){
        OrderProductDTO oProduct = new OrderProductDTO(oId, pId, option);
        return oProductService.getOProduct(oProduct);
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    public OrderProductDTO addOProduct(@PathVariable int oId, @RequestBody OrderProductDTO oProduct){
        return oProductService.addOProduct(oProduct);
    }

    @RequestMapping(value="/{pId}/option/{option}", method=RequestMethod.PUT)
    public OrderProductDTO updateOProduct(@PathVariable int oId, @PathVariable int pId,
            @PathVariable String option, @RequestBody OrderProductDTO oProduct){
        return oProductService.updateOProductCnt(oProduct);
    }

    @RequestMapping(value="/{pId}/option/{option}", method=RequestMethod.DELETE)
    public void deleteOProduct(@PathVariable int oId,
            @PathVariable int pId, @PathVariable String option){
        OrderProductDTO oProduct = new OrderProductDTO(oId, pId, option);
        oProductService.deleteOProduct(oProduct);
    }
}
