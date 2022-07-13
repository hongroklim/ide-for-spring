package product.detail;

import java.lang.reflect.Method;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import config.SpringConfig;
import com.company.dto.ProductDetailDTO;
import com.company.product.detail.ProductDetailService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("rawtypes")
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

    @Test
    public void verifyOptionCd() throws Exception {
        ProductDetailDTO pDetail = new ProductDetailDTO(2, "010203");
        
        Class[] paramClass = new Class[]{ProductDetailDTO.class};
        Method m = pDetailService.getClass().getDeclaredMethod("verifyOptionCd", paramClass);
        m.setAccessible(true);
        m.invoke(pDetailService, pDetail);
    }

    @Test(expected=Exception.class)
    public void verifyOptionCdWithException() throws Exception {
        ProductDetailDTO pDetail = new ProductDetailDTO(2, "010400");
        
        Class[] paramClass = new Class[]{ProductDetailDTO.class};
        Method m = pDetailService.getClass().getDeclaredMethod("verifyOptionCd", paramClass);
        m.setAccessible(true);
        m.invoke(pDetailService, pDetail);
    }

}