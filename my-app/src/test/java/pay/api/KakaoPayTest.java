package pay.api;

import config.SpringConfig;
import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.PayApiDTO;
import dev.rokong.dto.PayStatusDTO;
import dev.rokong.dto.PayTypeDTO;
import dev.rokong.mock.MockObjects;
import dev.rokong.pay.api.KakaoPayService;
import dev.rokong.pay.api.PayApiDAO;
import dev.rokong.pay.type.PayTypeService;
import dev.rokong.util.ObjUtil;
import dev.rokong.util.RandomUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class KakaoPayTest extends SpringConfig {

    @Autowired @Qualifier("kakaoPayService")
    private KakaoPayService kakaoPayService;

    @Autowired
    private PayTypeService payTypeService;

    @Autowired
    private PayApiDAO payApiDAO;

    @Autowired
    private MockObjects mObj;

    @Test
    public void payTypeId(){
        int id = 8;     //in database
        PayTypeDTO payType = payTypeService.getPayTypeNotNull(id);
        assertThat(payType.getType(), is(equalTo("API")));
        assertThat(payType.getOption1(), is(equalTo("카카오페이")));

        assertThat(kakaoPayService.getPayTypeId(), is(equalTo(id)));
    }

    //no authentication key from API
    @Test(expected = RuntimeException.class)
    public void preparePay(){
        //create order
        OrderDTO order = mObj.order.any();

        //append order products
        mObj.oProduct.anyList(3);

        String redirectURL = kakaoPayService.preparePay(order.getId());

        assertThat(ObjUtil.isNotEmpty(redirectURL), is(equalTo(true)));
    }

    private void insertPayApi(int OrderId){
        PayApiDTO payApi = new PayApiDTO();
        payApi.setOrderId(OrderId);
        payApi.setApiKey(RandomUtil.randomString(10));
        payApi.setApiName("KAKAOPAY");
        payApiDAO.insertPayApi(payApi);
    }

    @Test(expected = RuntimeException.class)
    public void getPayStatus(){
        //insert new payApi
        OrderDTO order = mObj.order.any();
        this.insertPayApi(order.getId());

        //test
        PayStatusDTO payStatus = kakaoPayService.getPayStatus(order.getId());

        assertThat(payStatus.getOrderId(), is(equalTo(order.getId())));
    }

    @Test(expected = RuntimeException.class)
    public void approvePay(){
        //insert new payApi
        OrderDTO order = mObj.order.any();
        this.insertPayApi(order.getId());

        kakaoPayService.approvePay(order.getId(), null);
    }
}
