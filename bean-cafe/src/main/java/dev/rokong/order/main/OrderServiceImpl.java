package dev.rokong.order.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.rokong.dto.OrderDTO;
import dev.rokong.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired OrderDAO orderDAO;

    public OrderDTO getOrder(int id){
        return null;    //select order
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

    public OrderDTO createOrder(OrderDTO order){
        this.getOrderNotNull(order);

        /*

        order process
        1. init order : create primary key
        2. add products
        3. add others info in order
        4. add delivery
        5. order complete

        */
        
        return this.getOrderNotNull(order);
    }

    public OrderDTO updateOrder(OrderDTO order){
        this.getOrderNotNull(order);

        return this.getOrderNotNull(order);
    }

    public void deleteOrder(int id){
        this.getOrderNotNull(id);
    }
}
