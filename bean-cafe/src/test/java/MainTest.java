import static org.junit.Assert.*;
import static org.hamcrest.core.IsEqual.equalTo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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