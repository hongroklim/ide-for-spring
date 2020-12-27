package dev.rokong.delivery;

import dev.rokong.dto.DeliveryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order/{oId}/delivery")
@ResponseStatus(HttpStatus.OK)
public class DeliveryController {
    
    @Autowired
    DeliveryService deliveryService;

    @RequestMapping(value="", method=RequestMethod.GET)
    public DeliveryDTO getDelivery(@PathVariable int oId){
        return deliveryService.getDelivery(oId);
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    public DeliveryDTO createDelivery(@PathVariable int oId,
                                           @RequestBody DeliveryDTO delivery){
        return deliveryService.createDelivery(delivery);
    }

    @RequestMapping(value="", method=RequestMethod.PUT)
    public DeliveryDTO updateDelivery(@PathVariable int oId,
                                           @RequestBody DeliveryDTO delivery){
        return deliveryService.updateDelivery(delivery);
    }

    @RequestMapping(value="", method=RequestMethod.DELETE)
    public void deleteDelivery(@PathVariable int oId){
        deliveryService.deleteDelivery(oId);
    }

}