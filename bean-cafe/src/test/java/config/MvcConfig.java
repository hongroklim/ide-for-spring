package config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(value="test")
@ContextConfiguration
(locations={"file:src/main/webapp/WEB-INF/context/root-context.xml",
        "file:src/main/webapp/WEB-INF/context/dispatcher-servlet.xml"})
@WebAppConfiguration
@Transactional @Rollback
public class MvcConfig {
	
    @Autowired protected WebApplicationContext wac;

    protected MockMvc mvc;
    
    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
    
    @Test
    public void initialization(){
        assertThat(1, is(equalTo(1)));
    }
}