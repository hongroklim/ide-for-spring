package product.option;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import config.SpringConfig;
import dev.rokong.dto.ProductOptionDTO;
import dev.rokong.product.option.ProductOptionDAO;
import dev.rokong.product.option.ProductOptionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    @Test
    public void updateProductOptionOrder(){
        //asis Product option
        ProductOptionDTO asisPOption = new ProductOptionDTO(2, 3, "01");
        asisPOption = pOptionService.getPOptionNotNull(asisPOption);

        //tobe Product option
        ProductOptionDTO tobePOption = pOptionService.getPOptionNotNull(asisPOption);
        tobePOption.setOptionId("03");

        //list before update
        ProductOptionDTO param = new ProductOptionDTO(asisPOption);
        param.setOptionId("");
        List<ProductOptionDTO> list = pOptionService.getPOptionList(param);
        log.info("before list : "+list.toString());

        ProductOptionDTO result = pOptionService.updatePOption(asisPOption, tobePOption);

        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is(equalTo(asisPOption.getName())));
        assertThat(result.getOptionId(), is(equalTo("03")));

        //liset after update
        list = pOptionService.getPOptionList(param);
        log.info("after list : "+list.toString());
    }
}
