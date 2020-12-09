package dev.rokong.order.product.delivery;

import java.util.List;

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

    public void deleteOPDelivery(OrderProductDeliveryDTO oPDelivery){
        sqlSession.delete(PREFIX+"delete", oPDelivery);
    }

    public List<OrderProductDeliveryDTO> selectOPdeliveriesByOrder(int orderId){
        return sqlSession.selectList(PREFIX+"selectOPdeliveriesByOrder", orderId);
    }
}