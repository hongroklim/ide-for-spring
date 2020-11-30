package order.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import config.MvcUnitConfig;
import dev.rokong.order.main.OrderController;

public class ControllerTest extends MvcUnitConfig {

    @Autowired OrderController oController;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(oController).build();
    }

    //TODO make tests
    
}