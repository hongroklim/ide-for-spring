package order.main;

import config.SpringConfig;
import dev.rokong.annotation.OrderStatus;
import dev.rokong.dto.*;
import dev.rokong.mock.MockObjects;
import dev.rokong.order.delivery.OrderDeliveryDAO;
import dev.rokong.order.delivery.OrderDeliveryService;
import dev.rokong.order.main.OrderDAO;
import dev.rokong.order.main.OrderService;
import dev.rokong.order.product.OrderProductDAO;
import dev.rokong.order.product.OrderProductService;
import dev.rokong.product.main.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ServiceTest extends SpringConfig {

    @Autowired
    private MockObjects mockObj;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderProductService oProductService;

    @Autowired
    private OrderDeliveryService oDeliveryService;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderProductDAO oProductDAO;

    @Autowired
    private OrderDeliveryDAO oDeliveryDAO;

    @Autowired
    private ProductService productService;

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

    /**
     * preapre order status
     * "order" : OrderDTO, "oDelivery" : List(OrderDeliveryDTO), "oProduct" : List(OrderProductDTO)
     * <pre>
     *main             *
     *delivery     *       *
     *product    * * *     *
     * </pre>
     * @return hashMap includes order ,oDelivery, oProduct
     */
    public Map<String, Object> prepareOrderStatus() {
        Map<String, Object> result = new HashMap<>();

        OrderDTO order = mockObj.order.any();
        result.put("order", order);

        List<OrderProductDTO> oProdList = mockObj.oProduct.anyList(3);

        //get another product
        ProductDTO product = mockObj.product.temp();
        product.setDeliveryId(null);
        product = productService.createProduct(product);

        //insert another order product
        OrderProductDTO oProduct = new OrderProductDTO();
        oProduct.setOrderId(order.getId());
        oProduct.setProductId(product.getId());
        oProduct.setCnt(1);
        oProduct = oProductService.addOProduct(oProduct);

        oProdList.add(oProduct);
        result.put("oProduct", oProdList);

        List<OrderDeliveryDTO> oDlvrList = new ArrayList<>();

        //add order delivery from mock objects
        OrderDeliveryDTO oDelivery = new OrderDeliveryDTO();
        oDelivery.setOrderId(oProdList.get(0).getOrderId());
        oDelivery.setDeliveryId(oProdList.get(0).getDeliveryId());
        oDelivery = oDeliveryService.getODeliveryNotNull(oDelivery);
        oDlvrList.add(oDelivery);

        //add order delivery from created product
        oDelivery.setDeliveryId(product.getDeliveryId());
        oDelivery = oDeliveryService.getODeliveryNotNull(oDelivery);
        oDlvrList.add(oDelivery);

        result.put("oDelivery", oDlvrList);

        return result;
    }

    @Test
    public void updateOrderStatusByMain(){
        Map<String, Object> map = this.prepareOrderStatus();
        OrderDTO order = (OrderDTO) map.get("order");

        OrderStatus tobeStatus = OrderStatus.CHECKING;

        //update status
        order.setOrderStatus(tobeStatus);
        orderService.updateOrderStatus(order);

        //order main
        order = orderDAO.select(order.getId());
        assertThat(order.getOrderStatus(), is(equalTo(tobeStatus)));

        //order delivery
        List<OrderDeliveryDTO> oDlvrList = oDeliveryDAO.selectByOrder(order.getId());
        oDlvrList.forEach(d -> {
            assertThat(d.getOrderStatus(), is(equalTo(tobeStatus)));
        });

        //order product
        OrderProductDTO param = new OrderProductDTO(order.getId());
        List<OrderProductDTO> oProdList = oProductDAO.selectList(param);
        oProdList.forEach(p -> {
            assertThat(p.getOrderStatus(), is(equalTo(tobeStatus)));
        });
    }

    @Test
    public void updateOrderStatusByProduct(){
        Map<String, Object> map = this.prepareOrderStatus();
        List<OrderProductDTO> oProdList = (List<OrderProductDTO>) map.get("oProduct");

        //canceled write
        oProdList.subList(0, 3).forEach(p -> {
            p.setOrderStatus(OrderStatus.CANCELED_WRITE);
            oProductService.updateStatus(p);
        });

        //get order delivery
        OrderDeliveryDTO oDelivery = new OrderDeliveryDTO();
        oDelivery.setOrderId(oProdList.get(0).getOrderId());
        oDelivery.setDeliveryId(oProdList.get(0).getDeliveryId());
        oDelivery = oDeliveryService.getODeliveryNotNull(oDelivery);

        //all order products are updated, so it will be canceled
        assertThat(oDelivery.getOrderStatus(), is(equalTo(OrderStatus.CANCEL)));

        //get order
        OrderDTO order = orderService.getOrderNotNull(oProdList.get(0).getOrderId());

        //one order delivery is canceled, but another delivery is still writing
        assertThat(order.getOrderStatus(), is(equalTo(OrderStatus.WRITING)));

        //canceled write
        oProdList.subList(3, 4).forEach(p -> {
            p.setOrderStatus(OrderStatus.CANCELED_WRITE);
            oProductService.updateStatus(p);
        });

        //get order delivery
        oDelivery = new OrderDeliveryDTO();
        oDelivery.setOrderId(oProdList.get(3).getOrderId());
        oDelivery.setDeliveryId(oProdList.get(3).getDeliveryId());
        oDelivery = oDeliveryService.getODeliveryNotNull(oDelivery);

        //all order products are updated, so it will be canceled
        assertThat(oDelivery.getOrderStatus(), is(equalTo(OrderStatus.CANCEL)));

        //get order
        order = orderService.getOrderNotNull(oProdList.get(0).getOrderId());

        //all order deliveries are canceled
        assertThat(order.getOrderStatus(), is(equalTo(OrderStatus.CANCEL)));
    }
}
