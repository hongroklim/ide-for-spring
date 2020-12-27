package dev.rokong.mock;

import dev.rokong.delivery.DeliveryService;
import dev.rokong.dto.DeliveryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.rokong.dto.OrderDTO;
import dev.rokong.util.RandomUtil;

@Component("MockDelivery")
public class MockDelivery extends AbstractMockObject<DeliveryDTO> {

    @Autowired
    DeliveryService deliveryService;

    @Autowired MockOrder mOrder;
    
    @Override
    public DeliveryDTO temp() {
        DeliveryDTO delivery = new DeliveryDTO();

        OrderDTO order = mOrder.any();
        delivery.setOrderId(order.getId());
        delivery.setUserNm(order.getUserNm());

        delivery.setSenderNm(RandomUtil.randomString(5));
        delivery.setRecipientNm(RandomUtil.randomString(4));

        delivery.setZipCd(RandomUtil.randomInt(5));
        delivery.setAddress1("addr-"+RandomUtil.randomString(6));
        delivery.setAddress2("building-"+RandomUtil.randomString(5));

        delivery.setContact1("010-"+RandomUtil.randomInt(4)+"-"+RandomUtil.randomInt(4));
        delivery.setContact2("042-552-"+RandomUtil.randomInt(4));

        delivery.setMessage("message-"+RandomUtil.randomString(5));

        return delivery;
    }

    @Override
    protected DeliveryDTO createObjService(DeliveryDTO obj) {
        return deliveryService.createDelivery(obj);
    }

    @Override
    protected DeliveryDTO getObjService(DeliveryDTO obj) {
        return deliveryService.getDelivery(obj.getOrderId());
    }

    @Override
    protected DeliveryDTO tempNth(int i){
        DeliveryDTO delivery = this.temp();

        OrderDTO order = mOrder.anyList(i+1).get(i);
        delivery.setOrderId(order.getId());

        return delivery;
    }
    
}