package dev.rokong.order.delivery;

import java.util.List;

import dev.rokong.dto.OrderDeliveryDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDeliveryDAOImpl implements OrderDeliveryDAO {
    
    @Autowired SqlSession sqlSession;

    public static final String PREFIX = "dev.rokong.orderDelivery.";

    public OrderDeliveryDTO select(OrderDeliveryDTO oDelivery){
        return sqlSession.selectOne(PREFIX+"select", oDelivery);
    }

    public int count(OrderDeliveryDTO oDelivery){
        return sqlSession.selectOne(PREFIX + "count", oDelivery);
    }

    public void insert(OrderDeliveryDTO oDelivery){
        sqlSession.insert(PREFIX+"insert", oDelivery);
    }

    public void delete(OrderDeliveryDTO oDelivery){
        sqlSession.delete(PREFIX+"delete", oDelivery);
    }

    public void update(OrderDeliveryDTO oDelivery){
        sqlSession.update(PREFIX + "update", oDelivery);
    }

    public List<OrderDeliveryDTO> selectByOrder(int orderId){
        return sqlSession.selectList(PREFIX+"selectByOrder", orderId);
    }
}