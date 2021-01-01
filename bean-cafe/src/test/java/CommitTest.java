import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.OrderProductDTO;
import dev.rokong.dto.ProductDetailDTO;
import dev.rokong.order.main.OrderService;
import dev.rokong.order.product.OrderProductService;
import dev.rokong.product.detail.ProductDetailService;
import dev.rokong.product.main.ProductService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(value="test")
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/context/root-context.xml",
                "file:src/main/webapp/WEB-INF/context/dispatcher-servlet.xml"})
@WebAppConfiguration
@Transactional @Commit
public class CommitTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;

    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void initTest(){
        assertThat(1, is(equalTo(1)));
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderProductService oProductService;

    @Autowired
    private ProductDetailService pDetailService;

    //@Test
    public void addOrderProduct(){
        //get order
        OrderDTO order = orderService.getOrderNotNull(2);

        //get product detail
        ProductDetailDTO pDetail = new ProductDetailDTO(3, "0101");
        pDetail = pDetailService.getDetailNotNull(pDetail);

        OrderProductDTO oProduct = new OrderProductDTO(
                order.getId(), pDetail.getProductId(), pDetail.getOptionCd()
        );
        oProduct.setCnt(1);

        oProductService.addOProduct(oProduct);
    }
}
