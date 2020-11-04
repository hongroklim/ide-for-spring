package product.option;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import config.SpringConfig;
import dev.rokong.dto.ProductOptionDTO;
import dev.rokong.product.option.ProductOptionDAO;
import dev.rokong.product.option.ProductOptionService;

public class DaoTest extends SpringConfig {
    
    @Autowired ProductOptionService pOptionService;
    @Autowired ProductOptionDAO pOptionDAO;
    
    @Test
    public void updateProductOption(){
        //get any product option in any product
        int anyProductId = 2;
        ProductOptionDTO param = new ProductOptionDTO(anyProductId);
        ProductOptionDTO asis = pOptionService.getPOptionList(param).get(0);
        assertThat(asis.getProductId(), is(equalTo(anyProductId)));

        //update
        pOptionDAO.updateProductOption(asis, asis.getOptionId(), "new name");
        
        //then get product option
        ProductOptionDTO getPOption = pOptionService.getPOptionNotNull(asis);
        assertThat(getPOption.getName(), is(equalTo("new name")));
    }
}
