package pay.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import config.MvcConfig;
import config.SpringConfig;
import dev.rokong.pay.api.TossService;

public class TossTest extends MvcConfig {
    
    @Autowired TossService tService;

    @Test
    public void initialize(){
        assertThat(tService.objectMapper, is(notNullValue()));
    }

}