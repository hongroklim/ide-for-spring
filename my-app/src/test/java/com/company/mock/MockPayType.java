package com.company.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.annotation.PayType;
import com.company.dto.PayTypeDTO;
import com.company.pay.type.PayTypeService;
import com.company.util.RandomUtil;

@Component("MockPayType")
public class MockPayType extends AbstractMockObject<PayTypeDTO> {

    @Autowired PayTypeService pTypeService;

    @Override
    public PayTypeDTO temp() {
        PayTypeDTO payType = new PayTypeDTO();
        payType.setPayType(
            RandomUtil.randomItem(PayType.values())
        );
        payType.setOption1("opt1-"+RandomUtil.randomInt(2));
        payType.setOption2("opt2-"+RandomUtil.randomInt(2));
        payType.setEnabled(true);

        return payType;
    }

    @Override
    protected PayTypeDTO createObjService(PayTypeDTO obj) {
        return pTypeService.createPayType(obj);
    }

    @Override
    protected PayTypeDTO getObjService(PayTypeDTO obj) {
       return pTypeService.getPayType(obj.getId());
    }

}