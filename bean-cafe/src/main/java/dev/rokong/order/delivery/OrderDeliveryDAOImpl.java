package dev.rokong.order.delivery;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.rokong.dto.OrderDeliveryDTO;

@Repository
public class OrderDeliveryDAOImpl implements OrderDeliveryDAO {
    
    @Autowired SqlSession sqlSession;

    private static final String PREFIX = "dev.rokong.orderDelivery.";

    public OrderDeliveryDTO selectOrderDelivery(int orderId){
        return sqlSession.selectOne(PREFIX+"selectOrderDelivery", orderId);
    }

    public void insertOrderDelivery(OrderDeliveryDTO oDelivery){
        sqlSession.insert(PREFIX+"insertOrderDelivery", oDelivery);
    }

    public void updateOrderDelivery(OrderDeliveryDTO oDelivery){
        sqlSession.update(PREFIX+"updateOrderDelivery", oDelivery);
    }

    public void deleteOrderDelivery(int orderId){
        sqlSession.delete(PREFIX+"deleteOrderDelivery", orderId);
    }
}