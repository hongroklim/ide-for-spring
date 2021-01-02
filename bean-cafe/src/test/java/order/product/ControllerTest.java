package order.product;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;

import config.MvcUnitConfig;
import dev.rokong.dto.OrderProductDTO;
import dev.rokong.order.product.OrderProductController;
import dev.rokong.order.product.OrderProductService;
import dev.rokong.util.ObjUtil;

public class ControllerTest extends MvcUnitConfig {

    @Autowired OrderProductController oProductController;

    @Autowired OrderProductService oProductService;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(oProductController).build();
    }

    private String createURL(int oId, Integer pId, String option){
        StringBuffer sbuf = new StringBuffer();
        
        if(oId == 0){
            throw new IllegalArgumentException("order id is not defined");
        }

        sbuf.append("/order")
            .append("/").append(oId)
            .append("/product");
        
        if(ObjUtil.isNotEmpty(pId)){
            sbuf.append("/")
                .append(pId);
            
            if(ObjUtil.isNotEmpty(option)){
                sbuf.append("/")
                    .append(option);
            }
        }
        
        return sbuf.toString();
    }

    private String createURL(int orderId){
        return this.createURL(orderId, null, null);
    }

    private String createURL(OrderProductDTO oProduct){
        return this.createURL(
            oProduct.getOrderId(), oProduct.getProductId(), oProduct.getOptionCd()
        );
    }

    @Test
    public void addOrderProduct() throws Exception {
        OrderProductDTO oProduct = mockObj.oProduct.temp();

        String url = this.createURL(oProduct.getOrderId());
        OrderProductDTO res = this.reqAndResBody(
            url, RequestMethod.POST, oProduct, OrderProductDTO.class
        );

        assertThat(res, is(notNullValue()));
        assertThat(res.getOrderId(), is(equalTo(oProduct.getOrderId())));
        assertThat(res.getProductId(), is(equalTo(oProduct.getProductId())));
        assertThat(res.getOptionCd(), is(equalTo(oProduct.getOptionCd())));
    }

    @Test
    public void getOrderProductList() throws Exception {
        //create three order product
        OrderProductDTO oProduct = mockObj.oProduct.anyList(3).get(0);
        
        String url = this.createURL(oProduct.getOrderId());
        List<OrderProductDTO> res = this.reqAndResBodyList(
            url, RequestMethod.GET, null, OrderProductDTO.class
        );

        assertThat(res, is(notNullValue()));
        assertThat(res.size(), is(equalTo(3)));
        assertThat(res.get(0).getOrderId(), is(equalTo(oProduct.getOrderId())));
    }

    @Test
    public void getOrderProduct() throws Exception {
        //create three order product
        OrderProductDTO oProduct = mockObj.oProduct.anyList(3).get(0);

        String url = this.createURL(oProduct);
        OrderProductDTO res = this.reqAndResBody(
            url, RequestMethod.GET, null, OrderProductDTO.class
        );

        assertThat(res, is(notNullValue()));
        assertThat(res.getOrderId(), is(equalTo(oProduct.getOrderId())));
        assertThat(res.getProductId(), is(equalTo(oProduct.getProductId())));
        assertThat(res.getOptionCd(), is(equalTo(oProduct.getOptionCd())));
    }

    @Test
    public void updateOrderProductCount() throws Exception {
        OrderProductDTO oProduct = mockObj.oProduct.any();

        int tobeCnt = oProduct.getCnt() + 1;
        oProduct.setCnt(tobeCnt);

        String url = this.createURL(oProduct);
        OrderProductDTO res = this.reqAndResBody(
            url, RequestMethod.PUT, oProduct, OrderProductDTO.class
        );

        assertThat(res, is(notNullValue()));
        assertThat(res.getOrderId(), is(equalTo(oProduct.getOrderId())));
        assertThat(res.getProductId(), is(equalTo(oProduct.getProductId())));
        assertThat(res.getOptionCd(), is(equalTo(oProduct.getOptionCd())));

        //check updated count
        assertThat(res.getCnt(), is(equalTo(tobeCnt)));

    }
    
    @Test
    public void deleteOrderProduct() throws Exception {
        OrderProductDTO oProduct = mockObj.oProduct.any();

        String url = this.createURL(oProduct);
        this.reqAndResBody(url, RequestMethod.DELETE, null, null);

        oProduct.setDeliveryId(null);
        OrderProductDTO afterDelete = oProductService.getOProduct(oProduct);

        assertThat(afterDelete, is(nullValue()));
    }
}