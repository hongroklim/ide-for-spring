package order.delivery;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import dev.rokong.dto.OrderDeliveryDTO;
import dev.rokong.order.delivery.OrderDeliveryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import config.SpringConfig;
import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.ProductDeliveryDTO;
import dev.rokong.mock.MockObjects;

public class ServiceTest extends SpringConfig {

    @Autowired
    OrderDeliveryService oDeliveryService;

    @Autowired MockObjects mObj;

    @Test
    public void addODelivery(){
        //create order
        OrderDTO order = mObj.order.any();

        //create product delivery
        ProductDeliveryDTO pDelivery = mObj.pDelivery.any();

        //add order product delivery
        boolean isAdded = oDeliveryService.addODelivery(
            order.getId(), pDelivery.getId()
        );

        //if oDelivery doesn't already exists, then return true
        assertThat(isAdded, is(equalTo(true)));

        //get order product delivery
        OrderDeliveryDTO param = new OrderDeliveryDTO(order.getId(), pDelivery.getId());
        OrderDeliveryDTO oDelivery = oDeliveryService.getODeliveryNotNull(param);

        //verify primary values
        assertThat(oDelivery.getOrderId(), is(equalTo(order.getId())));
        assertThat(oDelivery.getDeliveryId(), is(equalTo(pDelivery.getId())));

        //verify other values
        assertThat(oDelivery.getTypeNm(), is(equalTo(pDelivery.getType())));
        assertThat(oDelivery.getDeliveryNm(), is(equalTo(pDelivery.getName())));
        assertThat(oDelivery.getDeliveryPrice(), is(equalTo(pDelivery.getPrice())));
    }

    @Test
    public void removeODelivery(){
        //create order
        OrderDTO order = mObj.order.any();

        //create product delivery
        ProductDeliveryDTO pDelivery = mObj.pDelivery.any();

        int orderId = order.getId();
        int deliveryId = pDelivery.getId();

        //add order product delivery
        oDeliveryService.addODelivery(
            orderId, deliveryId
        );

        //get order product delivery
        OrderDeliveryDTO oDelivery = new OrderDeliveryDTO(orderId, deliveryId);
        oDelivery = oDeliveryService.getODeliveryNotNull(oDelivery);

        boolean isRemoved = oDeliveryService.removeODelivery(orderId, deliveryId);

        //if there is no order products with this delivery id, then returns true
        assertThat(isRemoved, is(equalTo(true)));

        //oDelivery is deleted
        oDelivery = oDeliveryService.getODelivery(oDelivery);
        assertThat(oDelivery, is(nullValue()));
    }
}