package dev.rokong.product.option;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.rokong.dto.ProductOptionDTO;

@RestController
@RequestMapping("/product/{pId}/option")
public class ProductOptionController {
    
    @Autowired ProductOptionService pOptionService;

    @RequestMapping(value="", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<ProductOptionDTO> getPOptionsInProduct(@PathVariable int pId){
        ProductOptionDTO param = new ProductOptionDTO(pId);
        return pOptionService.getPOptionList(param);
    }

    @RequestMapping(value="/group/{groupId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductOptionDTO> getPOptionsInGroup(@PathVariable int pId,
            @PathVariable int groupId){
        ProductOptionDTO param = new ProductOptionDTO(pId, groupId);
        return pOptionService.getPOptionList(param);
    }

    @RequestMapping(value="/group/{groupId}/id/{optionId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductOptionDTO getPOption(@PathVariable int pId, @PathVariable int groupId,
            @PathVariable String optionId){
        ProductOptionDTO param = new ProductOptionDTO(pId, groupId, optionId);
        return pOptionService.getPOption(param);
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ProductOptionDTO createProductOption(@RequestBody ProductOptionDTO pOption){
        return null;
    }
}