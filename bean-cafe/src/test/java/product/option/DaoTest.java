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
    public void updateProductOptionOrder(){
        //asis Product option
        ProductOptionDTO asisPOption = new ProductOptionDTO(2, 1, "02");
        asisPOption = pOptionService.getPOptionNotNull(asisPOption);

        //tobe Product option
        ProductOptionDTO tobePOption = pOptionService.getPOptionNotNull(asisPOption);
        tobePOption.setOrd(4);

        //list before update
        ProductOptionDTO param = new ProductOptionDTO(asisPOption);
        param.setOptionId("");
        List<ProductOptionDTO> list = pOptionService.getPOptionList(param);
        log.info("before list : "+list.toString());

        ProductOptionDTO result = pOptionService.updatePOption(asisPOption, tobePOption);

        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is(equalTo(asisPOption.getName())));
        assertThat(result.getOrd(), is(equalTo(4)));

        //liset after update
        list = pOptionService.getPOptionList(param);
        log.info("after list : "+list.toString());
    }
}
