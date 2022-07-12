package com.company.order.product.delivery;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.company.dto.OrderProductDeliveryDTO;

@Repository
public class OrderProductDeliveryDAOImpl implements OrderProductDeliveryDAO {
    
    @Autowired SqlSession sqlSession;

    public static final String PREFIX = "com.company.orderProductDelivery.";

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