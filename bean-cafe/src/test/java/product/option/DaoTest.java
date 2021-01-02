package product.option;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import dev.rokong.mock.MockObjects;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import config.SpringConfig;
import dev.rokong.dto.ProductOptionDTO;
import dev.rokong.product.option.ProductOptionDAO;
import dev.rokong.product.option.ProductOptionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoTest extends SpringConfig {
    
    @Autowired
    private ProductOptionService pOptionService;
    @Autowired
    private ProductOptionDAO pOptionDAO;

    @Autowired
    private MockObjects mObj;

    @Test
    public void updateProductOptionOrder(){
        //asis Product option
        List<ProductOptionDTO> list = mObj.pOption.anyList(4);
        ProductOptionDTO asisPOption = list.get(0);

        //tobe Product option
        ProductOptionDTO tobePOption = pOptionService.getPOptionNotNull(asisPOption);
        int tobeOrd = (asisPOption.getOrd() == 1) ? 2 : asisPOption.getOrd()-1;
        tobePOption.setOrd(tobeOrd);

        //list before update
        ProductOptionDTO param = new ProductOptionDTO(asisPOption);
        param.setOptionId("");
        list = pOptionService.getPOptionList(param);
        log.info("before list : "+list.toString());

        ProductOptionDTO result = pOptionService.updatePOption(tobePOption);

        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is(equalTo(asisPOption.getName())));
        assertThat(result.getOrd(), is(equalTo(tobeOrd)));

        //list after update
        list = pOptionService.getPOptionList(param);
        log.info("after list : "+list.toString());
    }
}
