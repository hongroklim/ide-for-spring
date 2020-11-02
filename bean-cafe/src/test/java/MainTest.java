import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;

import config.MvcConfig;
import dev.rokong.annotation.PriceField;
import dev.rokong.dto.ProductDTO;

public class MainTest extends MvcConfig {
    @Test
    public void databaseConnect(){
        int a = 10;
        int b = 15;

        assertThat(a+5, equalTo(b));
    }

    @Test
    public void mockMvc() throws Exception {
        this.mvc.perform(get("/user"))
            .andDo(log())
            .andExpect(status().isOk());
    }

    @Test
    public void PriceField(){
        ProductDTO product = new ProductDTO();
        product.setPrice(1234);

        PriceField field = PriceField.price;
        int val = field.getValueFrom(product);

        assertThat(val, is(equalTo(product.getPrice())));
    }
}