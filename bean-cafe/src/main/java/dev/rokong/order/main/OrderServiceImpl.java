package dev.rokong.order.main;

import dev.rokong.dto.OrderProductDTO;
import dev.rokong.order.product.OrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.rokong.annotation.OrderStatus;
import dev.rokong.dto.OrderDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.pay.type.PayTypeService;
import dev.rokong.user.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDAO orderDAO;

    @Autowired
    OrderProductService oProductService;
    @Autowired
    UserService userService;
    @Autowired
    PayTypeService pTypeService;

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

    public OrderDTO initOrder(OrderDTO order) {
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
        if (order.getPayId() != null) {
            order.setId(id);
            this.updateOrderPay(order);
        }

        return this.getOrderNotNull(id);
    }

    public void updateOrderPrice(int id, int price){
        //used by order.product
        OrderDTO order = this.getOrderNotNull(id);
        order.setPrice(price);

        orderDAO.updateOrderPrice(order);
    }

    public void updateOrderDeliveryPrice(int id, int deliveryPrice){
        //used by order.product
        OrderDTO order = this.getOrderNotNull(id);
        order.setDeliveryPrice(deliveryPrice);

        orderDAO.updateOrderDeliveryPrice(order);
    }

    public void updateOrderPay(OrderDTO order){
        //order id and pay id required
        this.getOrderNotNull(order);

        //get pay name
        String payNm = pTypeService.getPayTypeFullNm(order.getPayId());
        order.setPayNm(payNm);

        orderDAO.updateOrderPay(order);
    }

    public void verifyUpdateOrderStatus(OrderStatus asisStatus, OrderStatus tobeStatus){
        //verify parameter
        if(asisStatus == null){
            throw new BusinessException("asis status is not defined");

        }else if(tobeStatus == null){
            throw new BusinessException("tobe status is not defined");
        }

        //get asis order status

        if(asisStatus.equals(tobeStatus)){
            //if order status is same
            log.debug("asis status : "+asisStatus.toString());
            log.debug("tobe order status : "+tobeStatus.toString());
            throw new BusinessException("tobe order status is not changed");
        }

        if(asisStatus.isCanceled()){
            //if order status is already canceled
            log.debug("asis status : "+asisStatus.toString());
            log.debug("tobe order status : "+tobeStatus.toString());
            throw new BusinessException("order is already canceled");
        }

        if(tobeStatus.isProcess()){
            //change to process
            if(asisStatus.nextProcess() != tobeStatus){
                log.debug("asis status : "+asisStatus.toString());
                log.debug("tobe order status : "+tobeStatus.toString());
                throw new BusinessException("tobe status doesn't match later asis one");
            }

        }else{
            //change to cancel
            if(tobeStatus.isCustomerCancel() && !asisStatus.isCustomerCanCancel()){
                log.debug("asis status : "+asisStatus.toString());
                log.debug("tobe order status : "+tobeStatus.toString());
                throw new BusinessException("customer can not cancel");

            }else if(tobeStatus.isSellerCancel() && !asisStatus.isSellerCanCancel()){
                log.debug("asis status : "+asisStatus.toString());
                log.debug("tobe order status : "+tobeStatus.toString());
                throw new BusinessException("seller can not cancel");

            }
        }
    }

    public OrderDTO updateOrderStatus(OrderDTO order){        
        OrderDTO getOrder = this.getOrderNotNull(order);

        //verify order status before change
        this.verifyUpdateOrderStatus(
            getOrder.getOrderStatus(), order.getOrderStatus()
        );

        //verify editor nm
        if(order.getOrderStatus().isSellerCancel()){
            //if order status is changed to seller cancel
            userService.getUserNotNull(order.getEditorNm());

        }else{
            //the other case is that customer becomes editor
            order.setEditorNm(getOrder.getUserNm());
        }

        //update
        orderDAO.updateOrderStatus(order);

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

    public String getOrderDesc(int id){
        this.getOrderNotNull(id);

        List<OrderProductDTO> list
                = oProductService.getOProducts(new OrderProductDTO(id));



        return null;
    }
}