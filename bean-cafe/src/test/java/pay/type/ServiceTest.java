package pay.type;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import config.SpringConfig;
import dev.rokong.dto.PayTypeDTO;
import dev.rokong.mock.MockObjects;
import dev.rokong.pay.type.PayTypeService;
import dev.rokong.util.ObjUtil;

public class ServiceTest extends SpringConfig {
    
    @Autowired PayTypeService pTypeService;

    @Autowired MockObjects mockObj;

    @Test
    public void createPayType(){
        PayTypeDTO payType = mockObj.payType.any();

        //insert
        PayTypeDTO result = pTypeService.createPayType(payType);

        assertThat(result.getId(), is(not(equalTo(0))));

        //get
        result = pTypeService.getPayType(result.getId());
        assertThat(result, is(not(nullValue())));
    }

    @Test
    public void payTypeList(){
        List<PayTypeDTO> list = pTypeService.getPayTypes();
        assertThat(ObjUtil.isNotEmpty(list), is(equalTo(true)));
    }

}