package swagger;

import config.RootConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(value="test")
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/context/root-context.xml",
        "file:src/main/webapp/WEB-INF/context/dispatcher-servlet.xml"})
@WebAppConfiguration
public class Initialize {

    @Test
    public void init(){

    }
}
