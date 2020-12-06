package order.main;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;

import config.MvcUnitConfig;
import dev.rokong.annotation.OrderStatus;
import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.PayTypeDTO;
import dev.rokong.order.main.OrderController;
import dev.rokong.order.main.OrderService;
import dev.rokong.util.ObjUtil;

public class ControllerTest extends MvcUnitConfig {

    @Autowired OrderController oController;
    @Autowired OrderService oService;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(oController).build();
    }

    @Test
    public void initializeOrder() throws Exception {
        OrderDTO order = mockObj.order.temp();

        OrderDTO res = this.reqAndResBody(
            "/order", RequestMethod.POST, order, OrderDTO.class
        );

        assertThat(res, is(notNullValue()));
        assertThat(res.getId(), is(greaterThan(0)));
        assertThat(res.getUserNm(), is(equalTo(order.getUserNm())));
    }
    
    @Test
    public void getorder() throws Exception {
        OrderDTO order = mockObj.order.any();

        OrderDTO res = this.reqAndResBody(
            "/order/"+order.getId(), RequestMethod.GET,
            null, OrderDTO.class
        );

        assertThat(res, is(notNullValue()));
        assertThat(ObjUtil.isDefined(res.getId()), is(equalTo(true)));
    }

    @Test
    public void updateOrderPay() throws Exception {
        OrderDTO order = mockObj.order.any();

        //tobe payType
        PayTypeDTO payType = mockObj.payType.anyList(2).get(1);

        //update
        String url = "/order/"+order.getId()+"/pay";
        this.reqAndResBody(url, RequestMethod.PUT, payType.getId(), null);

        OrderDTO afterOrder = oService.getOrderNotNull(order.getId());

        assertThat(afterOrder.getPayId(), is(not(equalTo(order.getPayId()))));
        assertThat(afterOrder.getPayId(), is(equalTo(payType.getId())));

    }

    @Test
    public void cancelOrderByCustomer() throws Exception {
        OrderDTO order = mockObj.order.any();

        String url = "/order/"+order.getId()+"/status";
        
        //this.reqAndResBody(url, RequestMethod.DELETE, order.getUserNm(), null);
        
        MockHttpServletRequestBuilder mockReqMethod = this.initMockRequest(url, RequestMethod.DELETE);

        this.mvc.perform(mockReqMethod
                    .contentType(MediaType.TEXT_PLAIN)  //because of contentType
                    .content(order.getUserNm()))
                .andDo(log())
                .andExpect(status().isOk())
                .andReturn();

        OrderDTO afterOrder = oService.getOrderNotNull(order.getId());

        assertThat(afterOrder.getOrderStatus(), is(equalTo(OrderStatus.CANCELED_WRITE)));
        assertThat(afterOrder.getEditorNm(), is(equalTo(order.getUserNm())));
    }
}