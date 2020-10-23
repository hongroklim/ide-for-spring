package config;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
(locations={"file:src/main/webapp/WEB-INF/context/root-context.xml",
	"file:src/main/webapp/WEB-INF/context/app-context.xml"})
@WebAppConfiguration
public abstract class MvcUnitConfig {

    protected MockMvc mvc;
    
    @Before
    public abstract void setMvc();
}