import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;

import config.MvcConfig;

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

}