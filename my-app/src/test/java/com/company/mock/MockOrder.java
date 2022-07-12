package com.company.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.annotation.OrderStatus;
import com.company.dto.OrderDTO;
import com.company.dto.PayTypeDTO;
import com.company.dto.UserDTO;
import com.company.order.main.OrderService;
import com.company.pay.type.PayTypeService;

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