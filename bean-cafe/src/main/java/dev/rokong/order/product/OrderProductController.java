package dev.rokong.order.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.rokong.dto.OrderProductDTO;

@RestController
@RequestMapping("/order/{oId}/product")
@ResponseStatus(HttpStatus.OK)
public class OrderProductController {
    
    @Autowired OrderProductService oProductService;

    @RequestMapping(value="", method=RequestMethod.GET)
    public List<OrderProductDTO> getOProducts(@PathVariable int oId){
        return null;
    }

    @RequestMapping(value="/{pId}/option/{option}", method=RequestMethod.GET)
    public OrderProductDTO getOProduct(@PathVariable int oId,
            @PathVariable int pId, @PathVariable String option){
        return null;    
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    public OrderProductDTO addOProduct(@PathVariable int oId){
        return null;
    }

    @RequestMapping(value="/{pId}/option/{option}", method=RequestMethod.PUT)
    public OrderProductDTO updateOProduct(@PathVariable int oId, @PathVariable int pId,
            @PathVariable String option, @RequestBody OrderProductDTO oProduct){
        return null;
    }

    @RequestMapping(value="/{pId}/option/{option}", method=RequestMethod.DELETE)
    public void deleteOProduct(@PathVariable int oId,
            @PathVariable int pId, @PathVariable String option){

    }
}
