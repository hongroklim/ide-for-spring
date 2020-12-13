package order.product.delivery;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import config.SpringConfig;
import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.OrderProductDeliveryDTO;
import dev.rokong.dto.ProductDeliveryDTO;
import dev.rokong.mock.MockObjects;
import dev.rokong.order.product.delivery.OrderProductDeliveryService;

public class ServiceTest extends SpringConfig {

    @Autowired OrderProductDeliveryService oPDeliveryService;

    @Autowired MockObjects mObj;

    @Test
    public void addOPDelivery(){
        //create order
        OrderDTO order = mObj.order.any();

        //create product delivery
        ProductDeliveryDTO pDelivery = mObj.pDelivery.any();

        //add order product delivery
        boolean isAdded = oPDeliveryService.addOPDelivery(
            order.getId(), pDelivery.getId()
        );

        //if oPDelivery doesn't already exists, then return true
        assertThat(isAdded, is(equalTo(true)));

        //get order product delivery
        OrderProductDeliveryDTO param = new OrderProductDeliveryDTO(order.getId(), pDelivery.getId());
        OrderProductDeliveryDTO oPDelivery = oPDeliveryService.getOPDeliveryNotNull(param);

        //verify primary values
        assertThat(oPDelivery.getOrderId(), is(equalTo(order.getId())));
        assertThat(oPDelivery.getDeliveryId(), is(equalTo(pDelivery.getId())));

        //verify other values
        assertThat(oPDelivery.getTypeNm(), is(equalTo(pDelivery.getType())));
        assertThat(oPDelivery.getDeliveryNm(), is(equalTo(pDelivery.getName())));
        assertThat(oPDelivery.getPrice(), is(equalTo(pDelivery.getPrice())));
    }

    @Test
    public void removeOPDelivery(){
        //create order
        OrderDTO order = mObj.order.any();

        //create product delivery
        ProductDeliveryDTO pDelivery = mObj.pDelivery.any();

        int orderId = order.getId();
        int deliveryId = pDelivery.getId();

        //add order product delivery
        oPDeliveryService.addOPDelivery(
            orderId, deliveryId
        );

        //get order product delivery
        OrderProductDeliveryDTO oPDelivery = new OrderProductDeliveryDTO(orderId, deliveryId);
        oPDelivery = oPDeliveryService.getOPDeliveryNotNull(oPDelivery);

        boolean isRemoved = oPDeliveryService.removeOPDelivery(orderId, deliveryId);

        //if there is no order products with this delivery id, then returns true
        assertThat(isRemoved, is(equalTo(true)));

        //oPDelivery is deleted
        oPDelivery = oPDeliveryService.getOPDelivery(oPDelivery);
        assertThat(oPDelivery, is(nullValue()));
    }
}