package dev.rokong.order.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.rokong.dto.OrderDTO;

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

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO updateOrder(@PathVariable int id){
        return null;
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO cancelOrder(@PathVariable int id){
        return null;
    }
}
