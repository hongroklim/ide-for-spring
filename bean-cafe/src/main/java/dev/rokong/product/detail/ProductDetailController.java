package dev.rokong.product.detail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.rokong.dto.ProductDetailDTO;

@RestController
@RequestMapping("/product/{pId}/detail")
@SuppressWarnings("unused")
public class ProductDetailController {
    
    @Autowired ProductDetailService pDetailService;

    @RequestMapping(value="", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDetailDTO> getDetailListInProduct(@PathVariable int pId){
        ProductDetailDTO param = new ProductDetailDTO(pId);
        return pDetailService.getDetails(param);
    }

    @RequestMapping(value="/group/{optionCd}", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDetailDTO> getDetailListInGroup(@PathVariable int pId,
            @PathVariable String optionCd){
        ProductDetailDTO param = new ProductDetailDTO(pId, optionCd);
        return pDetailService.getDetails(param);
    }

    @RequestMapping(value="/option/{optionCd}", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ProductDetailDTO getDetail(@PathVariable int pId, @PathVariable String optionCd){
        ProductDetailDTO param = new ProductDetailDTO(pId, optionCd);
        return pDetailService.getDetail(param);
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ProductDetailDTO createDetail(@RequestBody ProductDetailDTO pDetail){
        return pDetailService.createDetail(pDetail);
    }

    @RequestMapping(value="/option/{optionCd}", method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteDetail(@PathVariable int pId, @PathVariable String optionCd){
        ProductDetailDTO param = new ProductDetailDTO(pId, optionCd);
        pDetailService.deleteDetail(param);
    }

    @RequestMapping(value="/option/{optionCd}", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ProductDetailDTO updateDetail(@PathVariable int pId, @PathVariable String optionCd,
            @RequestBody ProductDetailDTO pDetail){
        return pDetailService.updateDetail(pDetail);
    }

}