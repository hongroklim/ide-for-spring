package order.product;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

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

        //TODO compare all delivery ids in list
        assertThat(pList.get(0).getDeliveryId(),
            is(not(equalTo(pList.get(1).getDeliveryId()))));

        //TODO lamda expression
        int expected = 0;
        for(ProductDTO p : pList){
            expected += p.getDeliveryPrice();
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
}