package dev.rokong.order.main;

import com.sun.corba.se.impl.resolver.ORBDefaultInitRefResolverImpl;
import dev.rokong.dto.OrderDeliveryDTO;
import dev.rokong.dto.OrderProductDTO;
import dev.rokong.order.delivery.OrderDeliveryService;
import dev.rokong.order.product.OrderProductService;
import dev.rokong.util.ObjUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
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
    OrderDeliveryService oDeliverySerivce;

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

    public void updateOrderPrice(int id){
        //used by order.product
        OrderDTO order = this.getOrderNotNull(id);

        int price = oDeliverySerivce.totalPrice(id);
        order.setPrice(price);

        orderDAO.updateOrderPrice(order);
    }

    public void updateOrderDeliveryPrice(int id){
        //used by order.product
        OrderDTO order = this.getOrderNotNull(id);

        int deliveryPrice = oDeliverySerivce.totalDeliveryPrice(id);
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

    public OrderDTO updateOrderStatus(OrderDTO order){
        //update order status through main order
        OrderStatus tobeStatus = order.getOrderStatus();

        //verify tobe order status
        if (tobeStatus.isProcess()) {
            //if tobe status is normal process
            OrderStatus mainStatus = tobeStatus.getMainProcess();
            if (mainStatus != OrderStatus.WRITING
                    && mainStatus != OrderStatus.PAYMENT
                    && mainStatus != OrderStatus.CHECKING) {
                log.debug("tobe order status : {}", tobeStatus.name());
                throw new BusinessException("order status is not allowed in main order");
            }
        } else {
            //if tobe status is cancel
            if(tobeStatus != OrderStatus.CANCELED_WRITE
                    && tobeStatus != OrderStatus.CANCELED_PAYMENT){
                log.debug("tobe order status : {}", tobeStatus.name());
                throw new BusinessException("order status is not allowed in main order");
            }
        }

        //check order exists
        this.getOrderNotNull(order);

        //set editor name as customer and update
        orderDAO.updateOrderStatus(order);

        //update order product in specific status
        if(tobeStatus == OrderStatus.CHECKING || tobeStatus.isCanceled()){
            oDeliverySerivce.updateStatusByOrder(order.getId(), tobeStatus);
        }

        return this.getOrderNotNull(order);
    }

    public void updateOrderStatus(int id){
        //referred by order product

        //verify parameter
        if (id == 0) {
            throw new BusinessException("order id is not defined");
        }

        //get existing order
        OrderDTO order = this.getOrderNotNull(id);

        //get tobe order status
        OrderStatus tobeStatus = oProductService.getProperOrderStatus(id);

        if(order.getOrderStatus() != tobeStatus){
            //update only status is changed
            order.setOrderStatus(tobeStatus);
            orderDAO.updateOrderStatus(order);
        }
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

        orderDAO.updateOrderStatus(order);
    }

    public String getOrderDesc(int id){
        this.getOrderNotNull(id);

        List<OrderProductDTO> list
                = oProductService.getOProducts(new OrderProductDTO(id));

        StringBuffer sbuf = new StringBuffer();

        //order product is not exists
        if(ObjUtil.isEmpty(list)){
            return "";
        }

        sbuf.append(list.get(0).getProductNm());
        if(list.size() > 1){
            sbuf.append(" 외 ")
                    .append(list.size()-1)
                    .append("건");
        }

        return sbuf.toString();
    }
}