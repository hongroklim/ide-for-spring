package dev.rokong.mock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.rokong.annotation.OrderStatus;
import dev.rokong.dto.OrderDTO;
import dev.rokong.dto.UserDTO;
import dev.rokong.order.main.OrderService;

@Component("MockOrder")
public class MockOrder {
    
    private List<OrderDTO> orderList = new ArrayList<OrderDTO>();

    @Autowired OrderService oService;

    @Autowired MockUser mUser;

    public OrderDTO tempOrder(){
        OrderDTO order = new OrderDTO();
        
        UserDTO user = mUser.anyUser();
        order.setUserNm(user.getUserNm());
        order.setEditorNm(user.getUserNm());

        order.setPrice(0);
        order.setDeliveryPrice(0);

        //TODO mock paytype
        order.setPayId(0);
        order.setPayNm("");

        order.setOrderStatus(OrderStatus.WRITING);

        return order;
    }

    public OrderDTO createOrder(){
        return oService.initOrder(this.tempOrder());
    }

    private boolean isValidList(){
        if(this.orderList.size() == 0){
            return true;
        }else{
            return oService.getOrder(this.orderList.get(0).getId()) != null;
        }
    }

    private void validatingList(){
        if(!this.isValidList()){
            this.orderList.clear();
        }
    }

    public OrderDTO anyOrder(){
        this.validatingList();

        if(this.orderList.size() == 0){
            this.orderList.add(this.createOrder());
        }
        return this.orderList.get(0);
    }

    public List<OrderDTO> anyOrderList(int count){
        this.validatingList();
        while(this.orderList.size() < count){
            this.orderList.add(this.createOrder());
        }
        return this.orderList.subList(0, count);
    }

}