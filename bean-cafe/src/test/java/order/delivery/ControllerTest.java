package order.delivery;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;

import config.MvcUnitConfig;
import dev.rokong.dto.OrderDeliveryDTO;
import dev.rokong.order.delivery.OrderDeliveryController;
import dev.rokong.order.delivery.OrderDeliveryService;
import dev.rokong.util.RandomUtil;

public class ControllerTest extends MvcUnitConfig {

    @Autowired OrderDeliveryController oDeliveryController;
    @Autowired OrderDeliveryService oDeliveryService;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(oDeliveryController).build();
    }

    @Test
    public void mockTest(){
        OrderDeliveryDTO oDelivery = mockObj.oDelivery.any();
        //verify mock object
        assertThat(oDelivery, is(notNullValue()));
        assertThat(oDelivery.getZipCd(), is(notNullValue()));
        assertThat(oDelivery.getZipCd(), is(greaterThan((0))));
        
        List<OrderDeliveryDTO> list = mockObj.oDelivery.anyList(3);
        //verify list
        assertThat(list, is(notNullValue()));
        assertThat(list.size(), is(equalTo(3)));
        assertThat(list.get(0).getOrderId(), is(equalTo(oDelivery.getOrderId())));
    }

    private String createURL(OrderDeliveryDTO oDelivery){
        StringBuffer sbuf = new StringBuffer();

        sbuf.append("/order")
            .append("/").append(oDelivery.getOrderId())
            .append("/").append("delivery");

        return sbuf.toString();
    }

    @Test
    public void getOrderDelivery() throws Exception {
        OrderDeliveryDTO oDelivery = mockObj.oDelivery.any();

        String url = this.createURL(oDelivery);
        OrderDeliveryDTO res = this.reqAndResBody(
            url, RequestMethod.GET, null, OrderDeliveryDTO.class
        );

        assertThat(res, is(notNullValue()));
        assertThat(res.getOrderId(), is(equalTo(oDelivery.getOrderId())));
        assertThat(res.getUserNm(), is(equalTo(oDelivery.getUserNm())));
    }

    @Test
    public void createOrderDelivery() throws Exception{
        OrderDeliveryDTO oDelivery = mockObj.oDelivery.temp();

        String url = this.createURL(oDelivery);
        OrderDeliveryDTO res = this.reqAndResBody(
            url, RequestMethod.POST, oDelivery, OrderDeliveryDTO.class
        );

        OrderDeliveryDTO getODelivery
            = oDeliveryService.getODeliveryNotNull(oDelivery.getOrderId());
        
        //confirm it is same objects
        assertThat(res, is(notNullValue()));
        assertThat(res.getOrderId(), is(equalTo(getODelivery.getOrderId())));
        assertThat(res.getUserNm(), is(equalTo(getODelivery.getUserNm())));

    }

    @Test
    public void updateOrderDelivery() throws Exception{
        OrderDeliveryDTO asis = mockObj.oDelivery.any();

        OrderDeliveryDTO tobe = asis;
        tobe.setSenderNm("new_sender"+RandomUtil.randomInt(1));
        tobe.setZipCd(RandomUtil.randomInt(5));
        tobe.setContact1("010-"+RandomUtil.randomInt(8));

        String url = this.createURL(tobe);
        OrderDeliveryDTO resp = this.reqAndResBody(
            url, RequestMethod.PUT, tobe, OrderDeliveryDTO.class
        );

        //pk is not changed
        assertThat(resp, is(notNullValue()));
        assertThat(resp.getOrderId(), is(equalTo(asis.getOrderId())));

        //tobe columns are changed
        assertThat(resp.getSenderNm(), is(equalTo(tobe.getSenderNm())));
        assertThat(resp.getZipCd(), is(equalTo(tobe.getZipCd())));
        assertThat(resp.getContact1(), is(equalTo(tobe.getContact1())));
    }

    @Test
    public void deleteOrderDelivery() throws Exception{
        OrderDeliveryDTO oDelivery = mockObj.oDelivery.any();

        String url = this.createURL(oDelivery);
        this.reqAndResBody(url, RequestMethod.DELETE, null, null);

        OrderDeliveryDTO getODelivery
            = oDeliveryService.getODelivery(oDelivery.getOrderId());

        assertThat(getODelivery, is(nullValue()));
    }
    
}