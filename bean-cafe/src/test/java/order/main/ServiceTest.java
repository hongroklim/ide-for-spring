package order.main;

import config.SpringConfig;
import dev.rokong.annotation.OrderStatus;
import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.OrderProductDTO;
import dev.rokong.mock.MockObjects;
import dev.rokong.order.delivery.OrderDeliveryService;
import dev.rokong.order.product.OrderProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ServiceTest extends SpringConfig {

    @Autowired
    private MockObjects mockObj;

    @Autowired
    private OrderProductService oProductService;

    @Autowired
    private OrderDeliveryService oDeliveryService;

    @Test
    public void getProperOrderStatus() {

        //create new order
        OrderDTO order = mockObj.order.any();

        //order status lists
        List<OrderStatus> statusList = new ArrayList<>();
        statusList.add(OrderStatus.CHECKING);
        statusList.add(OrderStatus.PRODUCT_READY);
        statusList.add(OrderStatus.CANCEL_CHECK);
        statusList.add(OrderStatus.CANCELED_WRITE);

        //add products
        List<OrderProductDTO> oProdList = mockObj.oProduct.anyList(4);

        //update status
        OrderProductDTO oProduct;
        for (int i = 0; i < oProdList.size(); i++) {
            //set parameter from order product and status list
            oProduct = oProdList.get(i);
            oProduct.setOrderStatus(statusList.get(i));

            oProductService.updateStatus(oProduct);
        }

        OrderStatus properStatus = oDeliveryService.getProperOrderStatus(order.getId());
        /*
         * (o) checking
         * ( ) product ready     - later than check
         * ( ) cancel check      - cancel
         * ( ) canceled write    - cancel
         * */
        assertThat(properStatus, is(equalTo(OrderStatus.CHECKING)));

        //update checking -> product ready
        oProduct = oProdList.get(0);
        oProduct.setOrderStatus(OrderStatus.PRODUCT_READY);
        oProductService.updateStatus(oProduct);

        properStatus = oDeliveryService.getProperOrderStatus(order.getId());
        /*
         * (o) product ready
         * (o) product ready
         * ( ) cancel check      - cancel
         * ( ) canceled write    - cancel
         * */
        assertThat(properStatus, is(equalTo(OrderStatus.PRODUCT_READY)));

        //update all canceled
        oProduct = oProdList.get(0);
        oProduct.setOrderStatus(OrderStatus.CANCEL_PRODUCT);
        oProductService.updateStatus(oProduct);

        oProduct = oProdList.get(1);
        oProduct.setOrderStatus(OrderStatus.CANCELED_PRODUCT);
        oProductService.updateStatus(oProduct);

        properStatus = oDeliveryService.getProperOrderStatus(order.getId());
        /*
         * ( ) cancel product    - cancel
         * ( ) canceled product  - cancel
         * ( ) cancel check      - cancel
         * ( ) canceled write    - cancel
         * */
        assertThat(properStatus, is(equalTo(OrderStatus.CANCEL)));
    }

    //TODO update order status and check delivery and products
}
