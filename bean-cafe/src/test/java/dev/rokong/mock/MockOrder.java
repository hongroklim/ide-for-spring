package dev.rokong.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.rokong.annotation.OrderStatus;
import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.PayTypeDTO;
import dev.rokong.dto.UserDTO;
import dev.rokong.order.main.OrderService;
import dev.rokong.pay.type.PayTypeService;

@Component("MockOrder")
public class MockOrder extends AbstractMockObject<OrderDTO> {
    
    @Autowired OrderService oService;
    @Autowired PayTypeService pTypeService;

    @Autowired MockUser mUser;
    @Autowired MockPayType mPayType;

    @Override
    public OrderDTO temp() {
        OrderDTO order = new OrderDTO();
        
        UserDTO user = mUser.any();
        order.setUserNm(user.getUserNm());
        order.setEditorNm(user.getUserNm());

        order.setPrice(0);
        order.setDeliveryPrice(0);

        PayTypeDTO payType = mPayType.any();
        String payNm = pTypeService.getPayTypeFullNm(payType.getId());
        order.setPayId(payType.getId());
        order.setPayNm(payNm);

        order.setOrderStatus(OrderStatus.WRITING);

        return order;
    }

    @Override
    protected OrderDTO createObjService(OrderDTO obj) {
        return oService.initOrder(obj);
    }

    @Override
    protected OrderDTO getObjService(OrderDTO obj) {
        return oService.getOrder(obj.getId());
    }

}