package product.main;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import config.SpringConfig;
import com.company.dto.ProductDTO;
import com.company.product.main.ProductDAO;
import com.company.product.main.ProductService;

public class DaoTest extends SpringConfig {
    
    @Autowired ProductService pService;
    @Autowired ProductDAO pDAO;

    @Test
    public void updateColumn(){
        pDAO.updateProductColumn(2, "price", 1111);
    }

    @Test
    public void updateProduct(){
        ProductDTO product = new ProductDTO();
        assertThat(product.getStockCnt(), is(nullValue()));
        assertThat(product.getEnabled(), is(nullValue()));

        pDAO.updateProduct(product);
    }
}
