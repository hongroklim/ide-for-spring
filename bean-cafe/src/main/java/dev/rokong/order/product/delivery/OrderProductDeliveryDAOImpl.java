package dev.rokong.order.product.delivery;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.rokong.dto.OrderProductDeliveryDTO;

@Repository
public class OrderProductDeliveryDAOImpl implements OrderProductDeliveryDAO {
    
    @Autowired SqlSession sqlSession;

    public static final String PREFIX = "dev.rokong.orderProductDelivery.";

    public OrderProductDeliveryDTO selectOPDelivery(OrderProductDeliveryDTO oPDelivery){
        return sqlSession.selectOne(PREFIX+"select", oPDelivery);
    }

    public void insertOPDelivery(OrderProductDeliveryDTO oPDelivery){
        sqlSession.insert(PREFIX+"insert", oPDelivery);
    }
}