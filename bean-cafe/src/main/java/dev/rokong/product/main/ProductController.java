package dev.rokong.product.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.rokong.dto.ProductDTO;

@RestController
@RequestMapping("/product")
public class ProductController {
    
    @Autowired ProductService pService;

    @RequestMapping(value="", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> getProductList(){
        return pService.getProductList();
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO createProduct(@RequestBody ProductDTO product){
        return pService.createProduct(product);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO getProduct(@PathVariable int id){
        return pService.getProduct(id);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO updateProduct(@RequestBody ProductDTO product){
        //name, categoryId, sellerNm, stockCnt, enabled
        return pService.updateProduct(product);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@PathVariable int id){
        pService.deleteProduct(id);
    }

}