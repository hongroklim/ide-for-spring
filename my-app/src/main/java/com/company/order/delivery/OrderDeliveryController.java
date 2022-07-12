package com.company.order.delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.company.dto.OrderDeliveryDTO;

@RestController
@RequestMapping("/order/{oId}/delivery")
@ResponseStatus(HttpStatus.OK)
public class OrderDeliveryController {
    
    @Autowired OrderDeliveryService oDeliveryService;

    @RequestMapping(value="", method=RequestMethod.GET)
    public OrderDeliveryDTO getOrderDelivery(@PathVariable int oId){
        return oDeliveryService.getODelivery(oId);
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    public OrderDeliveryDTO createOrderDelivery(@PathVariable int oId,
            @RequestBody OrderDeliveryDTO oDelivery){
        return oDeliveryService.createODelivery(oDelivery);
    }

    @RequestMapping(value="", method=RequestMethod.PUT)
    public OrderDeliveryDTO updateOrderDelivery(@PathVariable int oId,
            @RequestBody OrderDeliveryDTO oDelivery){
        return oDeliveryService.updateODelivery(oDelivery);
    }

    @RequestMapping(value="", method=RequestMethod.DELETE)
    public void deleteOrderDelivery(@PathVariable int oId){
        oDeliveryService.deleteODelivery(oId);
    }

}