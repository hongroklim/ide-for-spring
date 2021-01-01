package dev.rokong.product.option;

import java.util.List;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.rokong.dto.ProductOptionDTO;

@RestController
@RequestMapping(value= "/product/{productId}/option", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags={"Product Option"})
public class ProductOptionController {
    
    @Autowired
    private ProductOptionService pOptionService;

    @RequestMapping(value="", method=RequestMethod.GET)
    public List<ProductOptionDTO> getPOptionsInProduct(@PathVariable int productId){
        ProductOptionDTO param = new ProductOptionDTO(productId);
        return pOptionService.getPOptionList(param);
    }

    @RequestMapping(value="/{groupId}", method=RequestMethod.GET)
    public List<ProductOptionDTO> getPOptionsInGroup(@PathVariable int productId,
            @PathVariable int groupId){
        ProductOptionDTO param = new ProductOptionDTO(productId, groupId);
        return pOptionService.getPOptionList(param);
    }

    @RequestMapping(value="/{groupId}/{optionId}", method=RequestMethod.GET)
    public ProductOptionDTO getPOption(@PathVariable int productId, @PathVariable int groupId,
                                       @PathVariable String optionId){
        ProductOptionDTO param = new ProductOptionDTO(productId, groupId, optionId);
        return pOptionService.getPOption(param);
    }

    @RequestMapping(value="/group", method=RequestMethod.POST)
    public ProductOptionDTO createProductOptionGroup(@RequestBody ProductOptionDTO pOption){
        return pOptionService.createPOptionGroup(pOption);
    }

    @RequestMapping(value="/{groupId}", method=RequestMethod.POST)
    public ProductOptionDTO createProductOption(@RequestBody ProductOptionDTO pOption){
        return pOptionService.createPOption(pOption);
    }

    @RequestMapping(value="/{groupId}/{optionId}", method=RequestMethod.DELETE)
    public void deleteProductOption(@PathVariable int productId, @PathVariable int groupId,
                                    @PathVariable String optionId){
        ProductOptionDTO pOption = new ProductOptionDTO(productId, groupId, optionId);
        pOptionService.deletePOption(pOption);
    }

    @RequestMapping(value="/{groupId}/{optionId}", method=RequestMethod.PUT)
    public ProductOptionDTO updateProductOption(@PathVariable int productId, @PathVariable int groupId,
                                                @PathVariable String optionId, @RequestBody ProductOptionDTO pOption){
        ProductOptionDTO asisPOption = new ProductOptionDTO(productId, groupId, optionId);
        return pOptionService.updatePOption(asisPOption, pOption);
    }

    @RequestMapping(value="/group", method=RequestMethod.DELETE)
    public void deleteProductOptionGroup(@PathVariable int productId){
        ProductOptionDTO pOption = new ProductOptionDTO(productId);
        pOptionService.deletePOptionGroup(pOption);
    }

    @RequestMapping(value="/{groupId}", method=RequestMethod.PUT)
    public ProductOptionDTO updateProductOptionGroupOrder(@PathVariable int productId, @PathVariable int groupId,
                                                          @RequestBody ProductOptionDTO pOption){
        ProductOptionDTO asisPOption = new ProductOptionDTO(productId, groupId);
        return pOptionService.updatePOptionGroupOrder(asisPOption, pOption.getOptionGroup());
    }
}
