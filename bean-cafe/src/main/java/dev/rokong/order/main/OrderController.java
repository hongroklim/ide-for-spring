package dev.rokong.order.main;

import dev.rokong.annotation.OrderStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping(value="/order", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags={"Order"})
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    @ApiOperation(value="get order", notes="get order by order id [id]")
    public OrderDTO getOrder(@PathVariable int id){
        return orderService.getOrder(id);
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    @ApiOperation(value="create order", notes="create basic order information")
    public OrderDTO createOrder(@RequestBody OrderDTO order){
        return orderService.createOrder(order);
    }

    @RequestMapping(value="/{id}/status", method=RequestMethod.PUT)
    @ApiOperation(value="update order status", notes="update order status [id, orderStatus]")
    public OrderDTO updateOrderStatus(@PathVariable int id, @RequestBody OrderDTO order){
        order.setId(id);
        return orderService.updateOrderStatus(order);
    }

    @RequestMapping(value="/{id}/pay", method=RequestMethod.PUT)
    @ApiOperation(value="update order pay", notes="update order pay [id, payId]")
    public void updateOrderPay(@PathVariable int id, @RequestBody OrderDTO order){
        order.setId(id);
        orderService.updateOrderPay(order);
    }
}
