package dev.rokong.delivery;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.rokong.dto.DeliveryDTO;

@Repository
public class DeliveryDAOImpl implements DeliveryDAO {
    
    @Autowired SqlSession sqlSession;

    private static final String PREFIX = "dev.rokong.delivery.";

    public DeliveryDTO select(int orderId){
        return sqlSession.selectOne(PREFIX+"select", orderId);
    }

    public void insert(DeliveryDTO delivery){
        sqlSession.insert(PREFIX+"insert", delivery);
    }

    public void update(DeliveryDTO delivery){
        sqlSession.update(PREFIX+"update", delivery);
    }

    public void delete(int orderId){
        sqlSession.delete(PREFIX+"delete", orderId);
    }
}