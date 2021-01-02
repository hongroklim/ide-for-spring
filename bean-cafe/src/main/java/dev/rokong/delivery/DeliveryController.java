package dev.rokong.delivery;

import dev.rokong.dto.DeliveryDTO;
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

@RestController
@RequestMapping(value="/delivery/{orderId}", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags={"Delivery"})
public class DeliveryController {
    
    @Autowired
    private DeliveryService deliveryService;

    @RequestMapping(value="", method=RequestMethod.GET)
    @ApiOperation(value="get delivery", notes="get delivery by order id [orderId]")
    public DeliveryDTO getDelivery(@PathVariable int orderId){
        return deliveryService.getDelivery(orderId);
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    @ApiOperation(value="create delivery", notes="create delivery in specified order id [*]")
    public DeliveryDTO createDelivery(@PathVariable int orderId, @RequestBody DeliveryDTO delivery){
        delivery.setOrderId(orderId);
        return deliveryService.createDelivery(delivery);
    }

    @RequestMapping(value="", method=RequestMethod.PUT)
    @ApiOperation(value="update delivery", notes="update delivery [orderId]")
    public DeliveryDTO updateDelivery(@PathVariable int orderId, @RequestBody DeliveryDTO delivery){
        delivery.setOrderId(orderId);
        return deliveryService.updateDelivery(delivery);
    }

    @RequestMapping(value="", method=RequestMethod.DELETE)
    @ApiOperation(value="delete delivery", notes="delete delivery [orderId]")
    public void deleteDelivery(@PathVariable int orderId){
        deliveryService.deleteDelivery(orderId);
    }
}