package product.delivery;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import config.SpringConfig;
import dev.rokong.annotation.DeliveryType;
import dev.rokong.dto.ProductDeliveryDTO;
import dev.rokong.mock.MockObjects;
import dev.rokong.product.delivery.ProductDeliveryService;
import dev.rokong.util.RandomUtil;

public class ServiceTest extends SpringConfig {

    @Autowired ProductDeliveryService pDeliveryService;

    @Autowired MockObjects mObj;

    @Test
    public void initDefaultPDelivery(){
        //seller name and price required
        String sellerNm = mObj.user.any().getUserNm();
        int price = RandomUtil.randomInt(4);

        //test
        ProductDeliveryDTO pDelivery
            = pDeliveryService.initDefaultPDelivery(sellerNm, price);
        
        assertThat(pDelivery, is(notNullValue()));
        assertThat(pDelivery.getSellerNm(), is(equalTo(sellerNm)));
        assertThat(pDelivery.getPrice(), is(equalTo(price)));
    }

    public void createPDelivery(){
        ProductDeliveryDTO tmp = mObj.pDelivery.temp();

        //test
        ProductDeliveryDTO pDelivery
            = pDeliveryService.createPDelivery(tmp);

        assertThat(pDelivery, is(notNullValue()));
        assertThat(pDelivery.getId(), is(greaterThan(0)));
    }

    @Test
    public void getPDelivery(){
        //create 5 items
        List<ProductDeliveryDTO> list = mObj.pDelivery.anyList(5);
        
        //any item
        int anyId = RandomUtil.randomItem(list).getId();

        //test
        ProductDeliveryDTO pDelivery
            = pDeliveryService.getPDelivery(anyId);
        
        assertThat(pDelivery, is(notNullValue()));
        assertThat(pDelivery.getId(), is(equalTo(anyId)));
    }

    @Test
    public void updatePDelivery(){
        //get asis one
        ProductDeliveryDTO asis = mObj.pDelivery.any();
        assertThat(asis, is(notNullValue()));

        //get tobe deliveryType
        DeliveryType tobeType = null;
        for(DeliveryType d : DeliveryType.values()){
            if(asis.getDeliveryType() != d){
                tobeType = d;
                break;
            }
        }
        assertThat(tobeType, is(notNullValue()));

        String tobeName = "tobe-"+asis.getName();
        int tobePrice = asis.getPrice()+100;

        //create tobe object
        ProductDeliveryDTO tobe = new ProductDeliveryDTO();
        tobe.setId(asis.getId());
        tobe.setDeliveryType(tobeType);
        tobe.setName(tobeName);
        tobe.setPrice(tobePrice);

        //test
        ProductDeliveryDTO result
            = pDeliveryService.updatePDelivery(tobe);
        
        //verify
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(equalTo(asis.getId())));
        assertThat(result.getDeliveryType(), is(equalTo(tobe.getDeliveryType())));
        assertThat(result.getName(), is(equalTo(tobe.getName())));
        assertThat(result.getPrice(), is(equalTo(tobe.getPrice())));
    }

}