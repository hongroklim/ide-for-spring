package delivery;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.util.List;

import dev.rokong.dto.DeliveryDTO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;

import config.MvcUnitConfig;
import dev.rokong.delivery.DeliveryController;
import dev.rokong.delivery.DeliveryService;
import dev.rokong.util.RandomUtil;

public class ControllerTest extends MvcUnitConfig {

    @Autowired
    private DeliveryController deliveryController;
    @Autowired
    private DeliveryService deliveryService;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(deliveryController).build();
    }

    @Test
    public void mockTest(){
        DeliveryDTO delivery = mockObj.delivery.any();
        //verify mock object
        assertThat(delivery, is(notNullValue()));
        assertThat(delivery.getZipCd(), is(notNullValue()));
        assertThat(delivery.getZipCd(), is(greaterThan((0))));
        
        List<DeliveryDTO> list = mockObj.delivery.anyList(3);
        //verify list
        assertThat(list, is(notNullValue()));
        assertThat(list.size(), is(equalTo(3)));
        assertThat(list.get(0).getOrderId(), is(equalTo(delivery.getOrderId())));
    }

    private String createURL(DeliveryDTO delivery){
        StringBuffer sbuf = new StringBuffer();

        sbuf.append("/delivery")
            .append("/").append(delivery.getOrderId());

        return sbuf.toString();
    }

    @Test
    public void getOrderDelivery() throws Exception {
        DeliveryDTO delivery = mockObj.delivery.any();

        String url = this.createURL(delivery);
        DeliveryDTO res = this.reqAndResBody(
            url, RequestMethod.GET, null, DeliveryDTO.class
        );

        assertThat(res, is(notNullValue()));
        assertThat(res.getOrderId(), is(equalTo(delivery.getOrderId())));
        assertThat(res.getUserNm(), is(equalTo(delivery.getUserNm())));
    }

    @Test
    public void createOrderDelivery() throws Exception{
        DeliveryDTO delivery = mockObj.delivery.temp();

        String url = this.createURL(delivery);
        DeliveryDTO res = this.reqAndResBody(
            url, RequestMethod.POST, delivery, DeliveryDTO.class
        );

        DeliveryDTO getDelivery
            = deliveryService.getDeliveryNotNull(delivery.getOrderId());
        
        //confirm it is same objects
        assertThat(res, is(notNullValue()));
        assertThat(res.getOrderId(), is(equalTo(getDelivery.getOrderId())));
        assertThat(res.getUserNm(), is(equalTo(getDelivery.getUserNm())));

    }

    @Test
    public void updateOrderDelivery() throws Exception{
        DeliveryDTO asis = mockObj.delivery.any();

        DeliveryDTO tobe = asis;
        tobe.setSenderNm("new_sender"+RandomUtil.randomInt(1));
        tobe.setZipCd(RandomUtil.randomInt(5));
        tobe.setContact1("010-"+RandomUtil.randomInt(8));

        String url = this.createURL(tobe);
        DeliveryDTO resp = this.reqAndResBody(
            url, RequestMethod.PUT, tobe, DeliveryDTO.class
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
        DeliveryDTO delivery = mockObj.delivery.any();

        String url = this.createURL(delivery);
        this.reqAndResBody(url, RequestMethod.DELETE, null, null);

        DeliveryDTO getDelivery
            = deliveryService.getDelivery(delivery.getOrderId());

        assertThat(getDelivery, is(nullValue()));
    }
    
}