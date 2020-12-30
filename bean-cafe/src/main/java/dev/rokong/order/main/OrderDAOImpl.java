package dev.rokong.order.main;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.rokong.dto.OrderDTO;

@Repository
public class OrderDAOImpl implements OrderDAO {
    
    public static final String PREFIX = "dev.rokong.order.";

    @Autowired
    private SqlSessionTemplate sqlSession;
    
    public OrderDTO select(int id){
        return sqlSession.selectOne(PREFIX+"select", id);
    }

    public int count(int id){
        return sqlSession.selectOne(PREFIX+"count", id);
    }

    public int insert(OrderDTO order){
        sqlSession.insert(PREFIX+"insert", order);
        return order.getId();
    }

    public void updatePay(OrderDTO order){
        sqlSession.update(PREFIX+"updatePay", order);
    }

    public void updateOrderStatus(OrderDTO order){
        sqlSession.update(PREFIX+"updateOrderStatus", order);
    }

    public void updatePrice(OrderDTO order){
        sqlSession.update(PREFIX+"updatePrice", order);
    }

    public void updateDeliveryPrice(OrderDTO order){
        sqlSession.update(PREFIX+"updateDeliveryPrice", order);
    }
}