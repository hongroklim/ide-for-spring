package dev.rokong.order.main;

import dev.rokong.annotation.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.rokong.dto.OrderDTO;

@RestController
@RequestMapping("/order")
@ResponseStatus(HttpStatus.OK)
public class OrderController {
    
    @Autowired OrderService orderService;

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public OrderDTO getOrder(@PathVariable int id){
        return orderService.getOrder(id);
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO createOrder(@RequestBody OrderDTO order){
        return orderService.createOrder(order);
    }

    @RequestMapping(value="/{id}/pay", method=RequestMethod.PUT)
    public void updateOrderPay(@PathVariable int id, @RequestBody int payId){
        OrderDTO param = new OrderDTO(id);
        param.setPayId(payId);
        
        orderService.updateOrderPay(param);
    }

    @RequestMapping(value="/{id}/status", method=RequestMethod.POST)
    public OrderDTO updateOrderStatus(@PathVariable int id, @RequestBody OrderStatus orderStatus){
        OrderDTO order = new OrderDTO(id);
        order.setOrderStatus(orderStatus);
        return orderService.updateOrderStatus(order);
    }
}
