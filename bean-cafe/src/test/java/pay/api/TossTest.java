package pay.api;

import config.MvcConfig;
import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.PayApiDTO;
import dev.rokong.dto.PayStatusDTO;
import dev.rokong.dto.PayTypeDTO;
import dev.rokong.mock.MockObjects;
import dev.rokong.pay.api.PayApiDAO;
import dev.rokong.pay.api.TossService;
import dev.rokong.pay.type.PayTypeService;
import dev.rokong.util.ObjUtil;
import dev.rokong.util.RandomUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TossTest extends MvcConfig {
    //TODO extends test
    @Autowired @Qualifier("tossService")
    TossService tossService;

    @Autowired
    PayTypeService payTypeService;

    @Autowired
    PayApiDAO payApiDAO;

    @Autowired
    MockObjects mObj;

    @Test
    public void payTypeId(){
        int id = 7;     //in database
        PayTypeDTO payType = payTypeService.getPayTypeNotNull(id);
        assertThat(payType.getType(), is(equalTo("API")));
        assertThat(payType.getOption1(), is(equalTo("토스")));

        assertThat(tossService.getPayTypeId(), is(equalTo(id)));
    }

    //expected COMMON_INVALID_API_KEY from Toss
    @Test(expected = RuntimeException.class)
    public void makeRequest(){
        //create order
        OrderDTO order = mObj.order.any();

        //append order products
        mObj.oProduct.anyList(3);

        String redirectURL = tossService.preparePay(order.getId());

        assertThat(ObjUtil.isNotEmpty(redirectURL), is(equalTo(true)));
    }

    private void insertPayApi(int OrderId){
        PayApiDTO payApi = new PayApiDTO();
        payApi.setOrderId(OrderId);
        payApi.setApiKey(RandomUtil.randomString(10));
        payApi.setApiName("TOSS");
        payApiDAO.insertPayApi(payApi);
    }

    //expected COMMON_INVALID_API_KEY from Toss
    @Test(expected = RuntimeException.class)
    public void getPayStatus(){
        //insert new payApi
        OrderDTO order = mObj.order.any();
        this.insertPayApi(order.getId());

        //test
        PayStatusDTO payStatus = tossService.getPayStatus(order.getId());

        assertThat(payStatus.getOrderId(), is(equalTo(order.getId())));
    }

    @Test(expected = RuntimeException.class)
    public void approvePay(){
        //insert new payApi
        OrderDTO order = mObj.order.any();
        this.insertPayApi(order.getId());

        tossService.approvePay(order.getId(), null);
    }
}