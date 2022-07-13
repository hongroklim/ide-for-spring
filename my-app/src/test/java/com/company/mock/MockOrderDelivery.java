package com.company.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.dto.OrderDTO;
import com.company.dto.OrderDeliveryDTO;
import com.company.order.delivery.OrderDeliveryService;
import com.company.util.RandomUtil;

@Component("MockOrderDelivery")
public class MockOrderDelivery extends AbstractMockObject<OrderDeliveryDTO> {

    @Autowired OrderDeliveryService oDeliveryService;

    @Autowired MockOrder mOrder;
    
    @Override
    public OrderDeliveryDTO temp() {
        OrderDeliveryDTO oDelivery = new OrderDeliveryDTO();

        OrderDTO order = mOrder.any();
        oDelivery.setOrderId(order.getId());
        oDelivery.setUserNm(order.getUserNm());

        oDelivery.setSenderNm(RandomUtil.randomString(5));
        oDelivery.setRecipientNm(RandomUtil.randomString(4));

        oDelivery.setZipCd(RandomUtil.randomInt(5));
        oDelivery.setAddress1("addr-"+RandomUtil.randomString(6));
        oDelivery.setAddress2("building-"+RandomUtil.randomString(5));

        oDelivery.setContact1("010-"+RandomUtil.randomInt(4)+"-"+RandomUtil.randomInt(4));
        oDelivery.setContact2("042-552-"+RandomUtil.randomInt(4));

        oDelivery.setMessage("message-"+RandomUtil.randomString(5));

        return oDelivery;
    }

    @Override
    protected OrderDeliveryDTO createObjService(OrderDeliveryDTO obj) {
        return oDeliveryService.createODelivery(obj);
    }

    @Override
    protected OrderDeliveryDTO getObjService(OrderDeliveryDTO obj) {
        return oDeliveryService.getODelivery(obj.getOrderId());
    }

    @Override
    protected OrderDeliveryDTO tempNth(int i){
        OrderDeliveryDTO oDelivery = this.temp();

        OrderDTO order = mOrder.anyList(i+1).get(i);
        oDelivery.setOrderId(order.getId());

        return oDelivery;
    }
    
}