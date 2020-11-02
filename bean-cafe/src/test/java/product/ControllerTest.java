package product;

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
import org.springframework.web.util.NestedServletException;

import config.MvcUnitConfig;
import dev.rokong.category.CategoryService;
import dev.rokong.dto.CategoryDTO;
import dev.rokong.dto.ProductDTO;
import dev.rokong.dto.UserDTO;
import dev.rokong.product.ProductController;
import dev.rokong.product.ProductService;
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
        List<ProductDTO> pList = this.reqAndResBody("/product",
            RequestMethod.GET, null);
        
        assertThat(pList, is(notNullValue()));
        assertThat(pList.size(), is(greaterThan(0)));
    }

    private void setAnyCategoryAndSeller(ProductDTO product){
        //category
        List<CategoryDTO> cList = cService.getCategoryList();
        assertThat(cList, is(notNullValue()));
        assertThat(cList.size(), is(greaterThan(0)));
        product.setCategoryId(cList.get(0).getId());

        //seller
        List<UserDTO> uList = uService.getUsers();
        assertThat(uList, is(notNullValue()));
        assertThat(uList.size(), is(greaterThan(0)));
        product.setSellerNm(uList.get(0).getUserNm());
    }

    @Test
    public void createProduct() throws Exception {
        ProductDTO product = new ProductDTO();
        product.setName("test");
        product.setPrice(1234);
        product.setEnabled(true);
        product.setStockCnt(123);
        product.setDeliveryPrice(5678);
        product.setDiscountPrice(-123);
        this.setAnyCategoryAndSeller(product);

        ProductDTO getProduct = this.reqAndResBody("/product",
            RequestMethod.POST, product, ProductDTO.class);
        
        assertThat(getProduct, is(notNullValue()));
        product.setId(getProduct.getId());
        assertThat(product, equalTo(getProduct));
    }

    @Test
    public void getProduct() throws Exception {
        List<ProductDTO> pList = pService.getProductList();
        assertThat(pList, is(notNullValue()));
        assertThat(pList.size(), is(greaterThan(0)));

        ProductDTO req = pList.get(0);
        
        ProductDTO res = this.reqAndResBody("/product/"+req.getId(),
            RequestMethod.GET, null, ProductDTO.class);
        
        assertThat(res, is(notNullValue()));
        assertThat(res.getId(), is(equalTo(req.getId())));
    }

    private int randomIdx(int size){
        double d = Math.random();
        int i = (int) d*100;

        return i % size;
    }

    private ProductDTO getAnyProduct(){
        List<ProductDTO> pList = pService.getProductList();
        assertThat(pList, is(notNullValue()));
        assertThat(pList.size(), is(greaterThan(0)));

        int index = this.randomIdx(pList.size());
        ProductDTO result = pList.get(index);

        assertThat(result, is(notNullValue()));
        return result;
    }

    @Test
    public void updateProductName() throws Exception {
        ProductDTO asisProduct = this.getAnyProduct();
        String newName = "new Name TEST";

        ProductDTO product = asisProduct;
        product.setName(newName);

        ProductDTO res = this.reqAndResBody("/product/"+product.getId(),
            RequestMethod.PUT, product, ProductDTO.class);
        
        assertThat(res, is(notNullValue()));
        assertThat(res.getId(), is(equalTo(asisProduct.getId())));
        assertThat(res.getName(), is(equalTo(newName)));
    }

    @Test(expected=NestedServletException.class)
    public void deleteProduct() throws Exception {
        ProductDTO product = this.getAnyProduct();

        this.reqAndResBody("/product/"+product.getId(),
            RequestMethod.DELETE, null);
        //exception because of not deleted product options

        ProductDTO afterDelete = pService.getProduct(product.getId());
        assertThat(afterDelete, is(nullValue()));
    }

    @Test
    public void updateProductStock() throws Exception {
        ProductDTO product = this.getAnyProduct();
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
        ProductDTO product = this.getAnyProduct();
        Integer tobeCnt = null;
        product.setStockCnt(tobeCnt);

        ProductDTO res = this.reqAndResBody("/product/"+product.getId(),
            RequestMethod.PUT, product, ProductDTO.class);
        
        assertThat(res, is(notNullValue()));
        assertThat(res.getId(), is(equalTo(product.getId())));
        assertThat(res.getStockCnt(), is(nullValue()));
    }

}
