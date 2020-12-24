import config.MvcConfig;
import dev.rokong.pay.api.PayApiController;
import dev.rokong.pay.api.PayApiService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
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

    @Autowired
    private PayApiController payApiController;

    @Test
    public void reflectionTest(){
        Method getPayTypeId = null;
        String methodName = "getPayTypeId";

        try {
            getPayTypeId = PayApiService.class.getMethod(methodName, null);
        } catch (NoSuchMethodException e) {
            log.debug("method name : {}", methodName, e);
        }

        Field[] fields = PayApiController.class.getDeclaredFields();
        for(Field f : fields){
            //get only PayApiService
            if(f.getType().equals(PayApiService.class)){
                f.setAccessible(true);

                //get field in instance
                Object field = null;
                try {
                    field = f.get(payApiController);
                } catch (IllegalAccessException e) {
                    log.debug("field : {}", f.toString(), e);
                }

                //invoke
                int payTypeId = 0;
                try {
                    payTypeId = (int) getPayTypeId.invoke(field);
                } catch (ReflectiveOperationException e) {
                    log.debug("field : {}", f.toString(), e);
                }

                log.debug("field : {}, payTypeId : {}", f.toString(), payTypeId);
            }
        }
    }

}