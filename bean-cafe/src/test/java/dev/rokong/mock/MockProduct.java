package dev.rokong.mock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.rokong.dto.ProductDTO;
import dev.rokong.product.main.ProductService;

@Component("MockProduct")
public class MockProduct extends MockObjects {
    
    private List<ProductDTO> productList = new ArrayList<ProductDTO>();

    private @Autowired ProductService pService;

    private @Autowired MockUser user;
    private @Autowired MockCategory category;

    public ProductDTO tempProduct(){
        ProductDTO product = new ProductDTO();
        product.setName("product-"+this.randomString(5));
        product.setPrice(this.randomInt(5));
        product.setCategoryId(category.anyCategory().getId());
        product.setEnabled(true);
        product.setSellerNm(user.anyUser().getUserNm());
        product.setStockCnt(null);
        product.setDeliveryPrice(this.randomInt(5));
        product.setDiscountPrice(0);
        return product;
    }

    private ProductDTO createProduct(){
        ProductDTO product = this.tempProduct();
        return pService.createProduct(product);
    }

    private boolean isValidList(){
        if(this.productList.size() == 0){
            return true;
        }else{
            return pService.getProduct(this.productList.get(0).getId()) != null;
        }
    }

    private void validatingList(){
        if(!this.isValidList()){
            this.productList.clear();
        }
    }

    public ProductDTO anyProduct() {
        this.validatingList();

        if(this.productList.size() == 0){
            productList.add(this.createProduct());
        }
        return productList.get(0);
    }

    public List<ProductDTO> anyProductList(int count){
        this.validatingList();

        while(this.productList.size() < count){
            this.productList.add(this.createProduct());
        }
        return this.productList.subList(0, count);
    }
}