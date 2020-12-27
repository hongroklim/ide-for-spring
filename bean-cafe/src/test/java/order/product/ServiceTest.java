package order.product;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import dev.rokong.annotation.OrderStatus;
import dev.rokong.util.ObjUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import config.SpringConfig;
import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.OrderProductDTO;
import dev.rokong.dto.ProductDTO;
import dev.rokong.dto.ProductDetailDTO;
import dev.rokong.mock.MockObjects;
import dev.rokong.order.main.OrderService;
import dev.rokong.order.product.OrderProductService;
import dev.rokong.product.main.ProductService;
import dev.rokong.util.RandomUtil;

public class ServiceTest extends SpringConfig {
    
    @Autowired @Qualifier("MockObjects")
    protected MockObjects mockObj;

    @Autowired OrderService oService;
    @Autowired OrderProductService oProductService;

    @Autowired ProductService pService;

    @Test
    public void priceChangeAfterInit(){
        //create order
        OrderDTO order = mockObj.order.any();
        order = oService.getOrderNotNull(order.getId());

        //get original price in order
        int originalPrice = order.getPrice();
        assertThat(originalPrice, is(equalTo(0)));

        //create order product
        OrderProductDTO oProduct = mockObj.oProduct.temp();
        assertThat(order.getId(), is(equalTo(oProduct.getOrderId())));

        //create expected price by order product
        int cnt = oProduct.getCnt();
        int price = oProduct.getPrice();
        int expectedPrice = cnt * price;

        //insert order product
        oProduct = oProductService.addOProduct(oProduct);

        //get order after add product
        order = oService.getOrderNotNull(order.getId());
        assertThat(order.getPrice(), is(equalTo(expectedPrice)));
    }

    @Test
    public void priceChangeAfterAdd(){
        //create list of order product (3 element)
        List<OrderProductDTO> list = mockObj.oProduct.anyList(3);

        //get original price
        int orderId = list.get(0).getOrderId();
        OrderDTO asisOrder = oService.getOrderNotNull(orderId);
        int asisPrice = asisOrder.getPrice();

        //create 4th order product
        OrderProductDTO oProduct = mockObj.oProduct.temp();

        ProductDTO product = mockObj.product.any();
        ProductDetailDTO pDetail = mockObj.pDetail.anyList(4).get(3);

        assertThat(product.getId(), is(equalTo(pDetail.getProductId())));

        oProduct.setOptionCd(pDetail.getOptionCd());
        oProduct.setPrice(product.getPrice()+pDetail.getPriceChange());

        //insert order product
        oProduct = oProductService.addOProduct(oProduct);
        int productPrice = oProduct.getCnt() * oProduct.getPrice();

        //get price after add product
        OrderDTO tobeOrder = oService.getOrderNotNull(orderId);
        int afterPrice = tobeOrder.getPrice();

        //verify price in order
        assertThat(asisOrder.getId(), is(equalTo(tobeOrder.getId())));
        assertThat(asisPrice+productPrice, is(equalTo(afterPrice)));
        
    }

    private void addOrderProduct(int orderId, List<ProductDTO> productList){
        OrderProductDTO oProduct = null;
        for(ProductDTO p : productList){
            oProduct = new OrderProductDTO(orderId, p.getId());
            oProduct.setCnt(RandomUtil.randomInt(1)+1);
            oProductService.addOProduct(oProduct);
        }
    }

    @Test
    public void productsWithSameDeliveryId(){
        OrderDTO order = mockObj.order.any();

        int asisDeliveryPrice = order.getDeliveryPrice();
        assertThat(asisDeliveryPrice, is(equalTo(0)));

        //get product list
        List<ProductDTO> productList = mockObj.product.anyList(3);

        //three mock products have same delivery id
        int expectedDeliveryPrice = productList.get(0).getDeliveryPrice();

        //add products into order product
        this.addOrderProduct(order.getId(), productList);

        //get order after add products
        order = oService.getOrderNotNull(order.getId());
        
        int tobeDeliveryPrice = order.getDeliveryPrice();

        assertThat(tobeDeliveryPrice, is(equalTo(expectedDeliveryPrice)));
    }

    @Test
    public void productsWithDifferentDeliveryId(){
        //init order
        OrderDTO order = mockObj.order.any();
        
        //get three products with different delivery Id
        List<ProductDTO> pList = new ArrayList<ProductDTO>();
        for(int i=0; i<3; i++){
            pList.add(this.createProductWithNullDeliveryId());
        }

        int expected = 0;
        for(ProductDTO p : pList){
            //add prices into expected values
            expected += p.getDeliveryPrice();

            //compare all delivery ids in list
            for(ProductDTO p1 : pList){
                if(p.getId() != p1.getId()){
                    assertThat(p.getDeliveryId(), is(not(equalTo(p1.getDeliveryId()))));
                }
            }
        }

        //add products
        this.addOrderProduct(order.getId(), pList);

        //get order after add products
        order = oService.getOrderNotNull(order.getId());

        assertThat(order.getDeliveryPrice(), is(equalTo(expected)));
    }

    private ProductDTO createProductWithNullDeliveryId(){
        ProductDTO product = mockObj.product.temp();
        product.setDeliveryId(null);    //new delivery will be created

        return pService.createProduct(product);
    }

    @Test
    public void invalidateOrderProduct(){
        List<OrderProductDTO> list = mockObj.oProduct.anyList(3);
        OrderProductDTO oProduct = list.get(0);

        //get asis order
        OrderDTO order = oService.getOrderNotNull(oProduct.getOrderId());
        int asisPrice = order.getPrice();

        //set order status and invalidate
        oProduct.setOrderStatus(OrderStatus.CANCELED_WRITE);
        oProductService.updateOProductStatus(oProduct);

        int priceDiff = oProduct.getCnt() * (oProduct.getPrice()+oProduct.getDiscountPrice());

        //get tobe order
        order = oService.getOrderNotNull(oProduct.getOrderId());
        int tobePrice = order.getPrice();

        assertThat(asisPrice - tobePrice, is(equalTo(priceDiff)));
    }

    @Test
    public void skipInvalidateProduct(){
        //create order product
        List<OrderProductDTO> list = mockObj.oProduct.anyList(3);
        OrderProductDTO oProduct = list.get(0);

        //get asis count
        int asisCount = oProductService.countOProductsByDelivery(
                oProduct.getOrderId(), oProduct.getDeliveryId());

        //update invalid one
        oProduct.setOrderStatus(OrderStatus.CANCELED_WRITE);
        oProductService.updateOProductStatus(oProduct);

        //get tobe count
        int tobeCount = oProductService.countOProductsByDelivery(
                oProduct.getOrderId(), oProduct.getDeliveryId());

        //count is different -1
        assertThat(tobeCount, is(equalTo(asisCount-1)));
    }

    @Test
    public void updateOProductStatus(){
        //create order products
        OrderProductDTO oProduct = mockObj.oProduct.any();

        //update status
        OrderStatus tobeStatus = OrderStatus.CANCELED_WRITE;
        oProduct.setOrderStatus(tobeStatus);
        oProductService.updateOProductStatus(oProduct);

        //check order product
        OrderProductDTO getOProduct = oProductService.getOProductNotNull(oProduct);
        assertThat(getOProduct.getOrderStatus(), is(equalTo(tobeStatus)));

        //check order main
        OrderDTO order = oService.getOrderNotNull(oProduct.getOrderId());
        assertThat(order.getOrderStatus(), is(equalTo(OrderStatus.CANCEL))); }

    @Test
    public void updateOProductWithOrder(){
        //create list
        List<OrderProductDTO> list = mockObj.oProduct.anyList(3);

        //get one and update status
        OrderProductDTO oProduct = list.get(1);
        oProduct.setOrderStatus(OrderStatus.CANCELED_WRITE);
        oProductService.updateOProductStatus(oProduct);

        //others are not changed
        assertThat(list.get(0).getOrderStatus(), is(equalTo(OrderStatus.WRITING)));
        assertThat(list.get(2).getOrderStatus(), is(equalTo(OrderStatus.WRITING)));

        //check order main
        OrderDTO order = oService.getOrderNotNull(oProduct.getOrderId());
        //order status is not changed
        assertThat(order.getOrderStatus(), is(equalTo(OrderStatus.WRITING)));

        //update others
        oProduct = list.get(0);
        oProduct.setOrderStatus(OrderStatus.CANCELED_WRITE);
        oProductService.updateOProductStatus(oProduct);

        oProduct = list.get(2);
        oProduct.setOrderStatus(OrderStatus.CANCELED_WRITE);
        oProductService.updateOProductStatus(oProduct);

        //check order main
        order = oService.getOrderNotNull(oProduct.getOrderId());

        //after all products are changed, order status in main is also changed
        assertThat(order.getOrderStatus(), is(equalTo(OrderStatus.CANCEL)));
    }

    @Test
    public void updateStatusByOrder(){
        //create list
        List<OrderProductDTO> oProdList = mockObj.oProduct.anyList(4);

        //update one product invalid
        OrderProductDTO oProduct = oProdList.get(0);
        oProduct.setOrderStatus(OrderStatus.CANCELED_WRITE);
        oProductService.updateOProductStatus(oProduct);

        //update order to payment ready
        //TODO fix test into delivery
        oProductService.updateStatusByOrder(oProduct.getOrderId(), OrderStatus.PAYMENT_STANDBY);

        //refresh new lists in order
        OrderProductDTO param = new OrderProductDTO(oProduct.getOrderId());
        List<OrderProductDTO> newList = oProductService.getOProducts(param);

        for(OrderProductDTO p : newList){
            if(p.getProductId() == oProduct.getProductId()
                    && p.getOptionCd().equals(oProduct.getOptionCd())){
                //canceled product remains
                assertThat(p.getOrderStatus(), is(equalTo(oProduct.getOrderStatus())));
            }else{
                //valid products are changed
                assertThat(p.getOrderStatus(), is(equalTo(OrderStatus.PAYMENT_STANDBY)));
            }
        }

    }

    @Test
    public void getProperOrderStatus(){
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
        for(int i=0; i<oProdList.size(); i++){
            //set parameter from order product and status list
            oProduct = oProdList.get(i);
            oProduct.setOrderStatus(statusList.get(i));

            oProductService.updateOProductStatus(oProduct);
        }

        OrderStatus properStatus = oProductService.getProperOrderStatus(order.getId());
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
        oProductService.updateOProductStatus(oProduct);

        properStatus = oProductService.getProperOrderStatus(order.getId());
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
        oProductService.updateOProductStatus(oProduct);

        oProduct = oProdList.get(1);
        oProduct.setOrderStatus(OrderStatus.CANCELED_PRODUCT);
        oProductService.updateOProductStatus(oProduct);

        properStatus = oProductService.getProperOrderStatus(order.getId());
        /*
         * ( ) cancel product    - cancel
         * ( ) canceled product  - cancel
         * ( ) cancel check      - cancel
         * ( ) canceled write    - cancel
         * */
        assertThat(properStatus, is(equalTo(OrderStatus.CANCEL)));
    }
}