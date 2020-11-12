package product.detail;

import java.lang.reflect.Method;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import config.SpringConfig;
import dev.rokong.dto.ProductDetailDTO;
import dev.rokong.product.detail.ProductDetailService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceTest extends SpringConfig {
    
    @Autowired ProductDetailService pDetailService;

    @Test
    public void createFullName() throws Exception {
        Class[] paramClass = new Class[]{ProductDetailDTO.class};
        Method m = pDetailService.getClass().getDeclaredMethod("createFullName", paramClass);
        m.setAccessible(true);
        Object result = m.invoke(pDetailService, new ProductDetailDTO(2, "010201"));
        log.info("create full name : "+result.toString());
    }

}