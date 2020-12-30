package order.delivery;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import dev.rokong.annotation.OrderStatus;
import dev.rokong.dto.OrderDeliveryDTO;
import dev.rokong.dto.OrderProductDTO;
import dev.rokong.order.delivery.OrderDeliveryService;
import dev.rokong.order.product.OrderProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import config.SpringConfig;
import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.ProductDeliveryDTO;
import dev.rokong.mock.MockObjects;

import java.util.List;

public class ServiceTest extends SpringConfig {

    @Autowired
    OrderDeliveryService oDeliveryService;

    @Autowired
    OrderProductService oProductService;

    @Autowired MockObjects mObj;

    @Test
    public void addODelivery(){
        //create order
        OrderDTO order = mObj.order.any();

        //create product delivery
        ProductDeliveryDTO pDelivery = mObj.pDelivery.any();

        //add order product delivery
        oDeliveryService.addODelivery(order.getId(), pDelivery.getId());
        //if oDelivery doesn't already exists, then it will insert

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
        oDeliveryService.addODelivery(orderId, deliveryId);

        //get order product delivery
        OrderDeliveryDTO oDelivery = new OrderDeliveryDTO(orderId, deliveryId);
        oDeliveryService.checkODeliveryExist(oDelivery);

        oDeliveryService.removeODelivery(orderId, deliveryId);
        //if there is no order products with this delivery id, then it will be removed

        //oDelivery is not deleted, but status is changed to cancel
        oDelivery = oDeliveryService.getODelivery(oDelivery);
        assertThat(oDelivery, is(notNullValue()));
        assertThat(oDelivery.getOrderStatus(), is(equalTo(OrderStatus.CANCEL)));
    }

    @Test
    public void updateODeliveryStatus(){
        //create order products
        List<OrderProductDTO> oProdList = mObj.oProduct.anyList(3);
        OrderProductDTO oProduct = oProdList.get(0);

        OrderDeliveryDTO oDelivery = new OrderDeliveryDTO(oProduct.getOrderId(), oProduct.getDeliveryId());
        oDelivery = oDeliveryService.getODelivery(oDelivery);

        //at first, order delivery status is writing
        assertThat(oDelivery.getOrderStatus(), is(equalTo(OrderStatus.WRITING)));

        //update one product
        oProduct.setOrderStatus(OrderStatus.CANCELED_WRITE);
        oProductService.updateStatus(oProduct);

        //the other products are writing, so order delivery's status is not changed
        oDelivery = oDeliveryService.getODelivery(oDelivery);
        assertThat(oDelivery.getOrderStatus(), is(equalTo(OrderStatus.WRITING)));

        //update all product
        for(OrderProductDTO p : oProdList){
            p.setOrderStatus(OrderStatus.CANCELED_WRITE);
            oProductService.updateStatus(p);
        }

        oDelivery = oDeliveryService.getODelivery(oDelivery);

        //after cancel all products, order delivery is still remains
        assertThat(oDelivery, is(notNullValue()));
        assertThat(oDelivery.getOrderStatus(), is(equalTo(OrderStatus.CANCEL)));
    }
}