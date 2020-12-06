package order.product;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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

public class ServiceTest extends SpringConfig {
    
    @Autowired @Qualifier("MockObjects")
    protected MockObjects mockObj;

    @Autowired OrderService oService;
    @Autowired OrderProductService oProductService;

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

}