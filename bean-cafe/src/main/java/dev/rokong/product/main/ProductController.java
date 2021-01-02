package dev.rokong.product.main;

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

import dev.rokong.dto.ProductDTO;

@RestController
@RequestMapping(value="/product", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags={"Product"})
public class ProductController {
    
    @Autowired ProductService pService;

    @RequestMapping(value="", method=RequestMethod.GET)
    @ApiOperation(value="get product list", notes="get product list")
    public List<ProductDTO> getProductList(){
        return pService.getProductList();
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    @ApiOperation(value="create product", notes="create product [name, price, (categoryId), (enabled)," +
            "sellerNm, (stockCnt), (deliveryId), (deliveryPrice), (discountPrice)]")
    public ProductDTO createProduct(@RequestBody ProductDTO product){
        return pService.createProduct(product);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    @ApiOperation(value="get product", notes="get product by id")
    public ProductDTO getProduct(@PathVariable int id){
        return pService.getProduct(id);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    @ApiOperation(value="update product", notes="[(name), (price), (categoryId), (stockCnt), (enabled)," +
            "(deliveryId), (discountPrice)]")
    public ProductDTO updateProduct(@PathVariable int id, @RequestBody ProductDTO product){
        product.setId(id);
        return pService.updateProduct(product);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    @ApiOperation(value="delete product", notes="delete product")
    public void deleteProduct(@PathVariable int id){
        pService.deleteProduct(id);
    }

}