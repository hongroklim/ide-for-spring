package product.main;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;

import config.MvcUnitConfig;
import dev.rokong.category.CategoryService;
import dev.rokong.dto.ProductDTO;
import dev.rokong.product.main.ProductController;
import dev.rokong.product.main.ProductService;
import dev.rokong.user.UserService;

public class ControllerTest extends MvcUnitConfig {

    @Autowired ProductController pController;
    @Autowired ProductService pService;
    @Autowired UserService uService;
    @Autowired CategoryService cService;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(pController).build();
    }

    @Test
    public void pathVariableEnum() throws Exception {
        String url = "/product/"+2+"/discount";
        ProductDTO req = new ProductDTO();
        req.setId(2);
        req.setPrice(1234);

        this.reqAndResBody(url, RequestMethod.PUT, req, ProductDTO.class);
    }

    @Test
    public void getProductList() throws Exception {
        List<ProductDTO> pList = this.reqAndResBodyList("/product",
            RequestMethod.GET, null, ProductDTO.class);
        
        assertThat(pList, is(notNullValue()));
        assertThat(pList.size(), is(greaterThan(0)));
    }

    @Test
    public void createProduct() throws Exception {
        ProductDTO product = mockObj.product.temp();

        ProductDTO getProduct = this.reqAndResBody("/product",
            RequestMethod.POST, product, ProductDTO.class);
        
        assertThat(getProduct, is(notNullValue()));
        product.setId(getProduct.getId());
        assertThat(product, equalTo(getProduct));
    }

    @Test
    public void getProduct() throws Exception {
        ProductDTO req = mockObj.product.any();
        
        ProductDTO res = this.reqAndResBody("/product/"+req.getId(),
            RequestMethod.GET, null, ProductDTO.class);
        
        assertThat(res, is(notNullValue()));
        assertThat(res.getId(), is(equalTo(req.getId())));
    }

    @Test
    public void updateProductName() throws Exception {
        ProductDTO asisProduct = mockObj.product.any();
        String newName = "new Name TEST";

        ProductDTO product = asisProduct;
        product.setName(newName);

        ProductDTO res = this.reqAndResBody("/product/"+product.getId(),
            RequestMethod.PUT, product, ProductDTO.class);
        
        assertThat(res, is(notNullValue()));
        assertThat(res.getId(), is(equalTo(asisProduct.getId())));
        assertThat(res.getName(), is(equalTo(newName)));
    }

    @Test
    public void deleteProduct() throws Exception {
        ProductDTO product = mockObj.product.any();

        this.reqAndResBody("/product/"+product.getId(),
            RequestMethod.DELETE, null, null);
        //exception because of not deleted product options

        ProductDTO afterDelete = pService.getProduct(product.getId());
        assertThat(afterDelete, is(nullValue()));
    }

    @Test
    public void updateProductStock() throws Exception {
        ProductDTO product = mockObj.product.any();
        int tobeCnt = product.getStockCntInt() + 10;
        product.setStockCnt(tobeCnt);

        ProductDTO res = this.reqAndResBody("/product/"+product.getId(),
            RequestMethod.PUT, product, ProductDTO.class);
        
        assertThat(res, is(notNullValue()));
        assertThat(res.getId(), is(equalTo(product.getId())));
        assertThat(res.getStockCnt(), is(equalTo(tobeCnt)));    //stockCnt will be changed

        //the others are not change
        assertThat(res.getName(), is(equalTo(product.getName())));
        assertThat(res.getCategoryId(), is(equalTo(product.getCategoryId())));
        assertThat(res.getSellerNm(), is(equalTo(product.getSellerNm())));
        assertThat(res.getEnabled(), is(equalTo(product.getEnabled())));
    }

    @Test
    public void updateProductStockNull() throws Exception {
        ProductDTO product = mockObj.product.any();
        Integer tobeCnt = null;
        product.setStockCnt(tobeCnt);

        ProductDTO res = this.reqAndResBody("/product/"+product.getId(),
            RequestMethod.PUT, product, ProductDTO.class);
        
        assertThat(res, is(notNullValue()));
        assertThat(res.getId(), is(equalTo(product.getId())));
        assertThat(res.getStockCnt(), is(nullValue()));
    }

}
