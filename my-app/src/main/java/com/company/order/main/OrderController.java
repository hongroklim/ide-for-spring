package com.company.order.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.company.dto.OrderDTO;

@RestController
@RequestMapping("/order")
public class OrderController {
    
    @Autowired OrderService orderService;

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO getOrder(@PathVariable int id){
        return orderService.getOrder(id);
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO createOrder(@RequestBody OrderDTO order){
        return orderService.initOrder(order);
    }

    @RequestMapping(value="/{id}/pay", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateOrderPay(@PathVariable int id,
            @RequestBody int payId){
        OrderDTO param = new OrderDTO(id);
        param.setPayId(payId);
        
        orderService.updateOrderPay(param);
    }

    @RequestMapping(value="/{id}/status", method=RequestMethod.DELETE, consumes=MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void cancelOrder(@PathVariable int id,
            @RequestBody String user){
        orderService.cancelOrder(id, user);
    }
}
