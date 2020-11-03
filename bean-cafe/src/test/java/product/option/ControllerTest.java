package product.option;

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
    
    private ProductDTO getAnyProduct(){
        int id = 2;     //TODO get random id
        return pService.getProductNotNull(id);
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
}
