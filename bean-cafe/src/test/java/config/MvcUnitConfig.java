package config;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
(locations={"file:src/main/webapp/WEB-INF/context/root-context.xml",
	"file:src/main/webapp/WEB-INF/context/app-context.xml"})
@WebAppConfiguration
@Transactional @Rollback
public abstract class MvcUnitConfig {

    protected MockMvc mvc;
    
    @Autowired  @Qualifier("mvcMessageConverter")
    protected MappingJackson2HttpMessageConverter messageConverter;

    protected ObjectMapper objectMapper;

    @Before
    public void initObjectMapper(){
        this.objectMapper = messageConverter.getObjectMapper();
    }

    @Before
    public abstract void setMvc();
}