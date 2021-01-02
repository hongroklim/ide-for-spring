package dev.rokong.product.detail;

import java.util.List;

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

import dev.rokong.dto.ProductDetailDTO;

@RestController
@RequestMapping(value= "/product/{productId}/detail", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags={"Product Detail"})
public class ProductDetailController {
    
    @Autowired
    private ProductDetailService pDetailService;

    @RequestMapping(value="", method=RequestMethod.GET)
    @ApiOperation(value="get details in product", notes="get detail list in product")
    public List<ProductDetailDTO> getDetailListInProduct(@PathVariable int productId){
        ProductDetailDTO param = new ProductDetailDTO(productId);
        return pDetailService.getDetails(param);
    }

    @RequestMapping(value="/{optionCd}/sub", method=RequestMethod.GET)
    @ApiOperation(value="get details in option",
            notes="get detail list under option including itself")
    public List<ProductDetailDTO> getDetailListInGroup(@PathVariable int productId, @PathVariable String optionCd){
        ProductDetailDTO param = new ProductDetailDTO(productId, optionCd);
        return pDetailService.getDetails(param);
    }

    @RequestMapping(value="/{optionCd}", method=RequestMethod.GET)
    @ApiOperation(value="get detail", notes="get detail")
    public ProductDetailDTO getDetail(@PathVariable int productId, @PathVariable String optionCd){
        ProductDetailDTO param = new ProductDetailDTO(productId, optionCd);
        return pDetailService.getDetail(param);
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    @ApiOperation(value="create detail",
            notes="create product detail [productId, optionCd, (priceChange), (stockCnt), (enabled)]")
    public ProductDetailDTO createDetail(@RequestBody ProductDetailDTO pDetail){
        return pDetailService.createDetail(pDetail);
    }

    @RequestMapping(value="/{optionCd}", method=RequestMethod.DELETE)
    @ApiOperation(value="delete detail", notes="delete product detail")
    public void deleteDetail(@PathVariable int productId, @PathVariable String optionCd){
        ProductDetailDTO param = new ProductDetailDTO(productId, optionCd);
        pDetailService.deleteDetail(param);
    }

    @RequestMapping(value="/{optionCd}", method=RequestMethod.PUT)
    @ApiOperation(value="update detail",
            notes="update product detail [(priceChange), (stockCnt), (enabled)]")
    public ProductDetailDTO updateDetail(@PathVariable int productId, @PathVariable String optionCd,
                                         @RequestBody ProductDetailDTO pDetail){
        pDetail.setProductId(productId);
        pDetail.setOptionCd(optionCd);
        return pDetailService.updateDetail(pDetail);
    }
}