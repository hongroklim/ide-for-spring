package dev.rokong.order.product;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.rokong.dto.OrderProductDTO;

@Repository
public class OrderProductDAOImpl implements OrderProductDAO {
    
    public static final String PREFIX = "dev.rokong.orderProduct.";

    @Autowired SqlSessionTemplate sqlSession;

    public List<OrderProductDTO> selectList(OrderProductDTO oProduct){
        return sqlSession.selectList(PREFIX+"select", oProduct);
    }

    public OrderProductDTO select(OrderProductDTO oProduct){
        return sqlSession.selectOne(PREFIX+"select", oProduct);
    }
    
    public void insert(OrderProductDTO oProduct){
        sqlSession.insert(PREFIX+"insert", oProduct);
    }
    
    public void delete(OrderProductDTO oProduct){
        sqlSession.delete(PREFIX+"delete", oProduct);
    }
    
    public void updateCnt(OrderProductDTO oProduct){
        sqlSession.update(PREFIX+"updateCnt", oProduct);
    }

    public void updateToNull(OrderProductDTO oProduct){
        sqlSession.update(PREFIX+"updateToNull", oProduct);
    }

    public int countByDelivery(OrderProductDTO oProduct){
        return sqlSession.selectOne(PREFIX+"countByDelivery", oProduct);
    }

    public void updateValidAndStatus(OrderProductDTO oProduct){
        sqlSession.update(PREFIX+"updateValidAndStatus", oProduct);
    }
}