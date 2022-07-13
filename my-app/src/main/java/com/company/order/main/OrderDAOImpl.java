package com.company.order.main;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.company.dto.OrderDTO;

@Repository
public class OrderDAOImpl implements OrderDAO {
    
    public static final String PREFIX = "com.company.order.";

    @Autowired SqlSessionTemplate sqlSession;
    
    public OrderDTO selectOrder(int id){
        return sqlSession.selectOne(PREFIX+"selectOrder", id);
    }

    public int insertOrder(OrderDTO order){
        sqlSession.insert(PREFIX+"insertOrder", order);
        return order.getId();
    }

    public void updateOrderPay(OrderDTO order){
        sqlSession.update(PREFIX+"updateOrderPay", order);
    }

    public void updateOrderStatus(OrderDTO order){
        sqlSession.update(PREFIX+"updateOrderStatus", order);
    }

    public void updateOrderPrice(OrderDTO order){
        sqlSession.update(PREFIX+"updateOrderPrice", order);
    }

    public void updateOrderDeliveryPrice(OrderDTO order){
        sqlSession.update(PREFIX+"updateOrderDeliveryPrice", order);
    }
}