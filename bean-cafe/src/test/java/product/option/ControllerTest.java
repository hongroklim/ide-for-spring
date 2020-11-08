package product.option;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;

import config.MvcUnitConfig;
import dev.rokong.dto.ProductDTO;
import dev.rokong.dto.ProductOptionDTO;
import dev.rokong.product.main.ProductService;
import dev.rokong.product.option.ProductOptionController;
import dev.rokong.product.option.ProductOptionDAO;
import dev.rokong.product.option.ProductOptionService;

public class ControllerTest extends MvcUnitConfig {

    @Autowired ProductOptionController pOptionController;
    @Autowired ProductOptionService pOptionService;
    @Autowired ProductOptionDAO pOptionDAO;

    @Autowired ProductService pService;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(pOptionController).build();
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
    public void requestMapping() throws Exception {
        String url = "/product/"+2+"/option";
        List<ProductOptionDTO> res = this.reqAndResBody(url, RequestMethod.GET, null);
        
        url = "/product/"+2+"/option/group/"+1;
        res = this.reqAndResBody(url, RequestMethod.GET, null);

        url = "/product/"+2+"/option/group/"+1+"/id/"+"01";
        ProductOptionDTO res2 = this.reqAndResBody(url, RequestMethod.GET, null, ProductOptionDTO.class);
    }

    public void getPOptionsInProduct() throws Exception {

    }

    public void getPOptionsInGroup() throws Exception {
        
    }

    public void getPOption() throws Exception {
        
    }

    public void createProductOption() throws Exception {
        
    }

    public void deleteProductOption() throws Exception {
        
    }

    public void updateProductOption() throws Exception {
        
    }

    
}
