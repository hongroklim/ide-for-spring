package user;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import config.MvcUnitConfig;
import dev.rokong.user.UserController;

public class ControllerTest extends MvcUnitConfig{
    
    @Autowired UserController userController;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void initConfig(){
        assertThat(this.mvc, is(not(nullValue())));
    }
    
    @Test
    public void simpleRequest() throws Exception{
        this.mvc.perform(get("/user"))
            .andDo(log())
            .andExpect(status().isOk());
            //.andExpect(jsonPath("$", hasSize()));
    }
}