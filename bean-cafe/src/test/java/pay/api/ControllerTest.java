package pay.api;

import config.MvcUnitConfig;
import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.OrderProductDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.order.main.OrderService;
import dev.rokong.pay.api.PayApiController;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@Slf4j
public class ControllerTest extends MvcUnitConfig {

    @Autowired
    PayApiController payApiController;

    @Autowired
    OrderService orderService;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(payApiController).build();
    }

    @Test(expected = BusinessException.class)   //invalid API key
    public void channelingTest(){
        //create order product
        List<OrderProductDTO> pList = mockObj.oProduct.anyList(3);

        //update order pay
        int orderId = pList.get(0).getOrderId();
        int payId = 7;  //toss pay id
        OrderDTO order = new OrderDTO(orderId);
        order.setPayId(payId);
        orderService.updateOrderPay(order);

        order = orderService.getOrderNotNull(order.getId());
        assertThat(order, is(notNullValue()));
        assertThat(order.getPayId(), is(equalTo(payId)));

        //get method
        Method channelingApi = null;
        try {
            channelingApi = PayApiController.class.getDeclaredMethod("channelingApi", String.class, Object[].class);
        } catch (NoSuchMethodException e) {
            log.warn("method name : channelingApi");
            throw new RuntimeException(e);
        }

        //set accessible
        channelingApi.setAccessible(true);

        try {
            //create parameters
            Object[] objects = new Object[1];
            objects[0] = orderId;

            channelingApi.invoke(this.payApiController, "preparePay", objects);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
