package dev.rokong.order.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.rokong.annotation.OrderStatus;
import dev.rokong.dto.OrderDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.pay.type.PayTypeService;
import dev.rokong.user.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired OrderDAO orderDAO;

    @Autowired UserService userService;
    @Autowired PayTypeService pTypeService;

    public OrderDTO getOrder(int id){
        return orderDAO.selectOrder(id);
    }

    public OrderDTO getOrderNotNull(int id){
        OrderDTO order = this.getOrder(id);
        if(order == null){
            log.debug("order id : "+id);
            throw new BusinessException("order is not exists");
        }
        return order;
    }

    public OrderDTO getOrderNotNull(OrderDTO order){
        return this.getOrderNotNull(order.getId());
    }

    public OrderDTO initOrder(OrderDTO order){
        //initialize order

        /*
        order process
        1. init order : create primary key
        2. add products
        3. add others info in order
        4. add delivery
        5. order complete
        */

        //userNm is required
        userService.getUserNotNull(order.getUserNm());

        //set status
        order.setOrderStatus(OrderStatus.WRITING);

        //insert
        int id = orderDAO.insertOrder(order);

        //if payId exists, update it
        if(order.getPayId() != null){
            order.setId(id);
            this.updateOrderPay(order);
        }

        return this.getOrderNotNull(id);
    }

    public void updateOrderPrice(OrderDTO order){
        //used by order.product
        this.getOrderNotNull(order);

        //TODO calculate from order_product
    }

    public void updateOrderPay(OrderDTO order){
        //order id and pay id required
        this.getOrderNotNull(order);

        //get pay name
        String payNm = pTypeService.getPayTypeFullNm(order.getPayId());
        order.setPayNm(payNm);

        orderDAO.updateOrderPay(order);
    }

    public OrderDTO updateOrderStatus(OrderDTO order){
        return this.getOrderNotNull(order);
    }

    public void cancelOrder(int id, String user){
        OrderDTO order = this.getOrderNotNull(id);

        OrderStatus tobeStatus;

        if(order.getUserNm().equals(user)){
            //if editor is user
            tobeStatus = order.getOrderStatus().getCustomerCancel();
        }else{
            //if editor is seller
            tobeStatus = order.getOrderStatus().getSellerCancel();
        }

        //set status
        order.setOrderStatus(tobeStatus);
        order.setEditorNm(user);

        orderDAO.updateOrderStatus(order);
    }
}