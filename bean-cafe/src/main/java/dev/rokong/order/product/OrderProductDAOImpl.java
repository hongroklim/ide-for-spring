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

    public List<OrderProductDTO> selectOProductList(OrderProductDTO oProduct){
        return sqlSession.selectList(PREFIX+"selectOProduct", oProduct);
    }

    public OrderProductDTO selectOProduct(OrderProductDTO oProduct){
        return sqlSession.selectOne(PREFIX+"selectOProduct", oProduct);
    }
    
    public void insertOProduct(OrderProductDTO oProduct){
        sqlSession.insert(PREFIX+"insertOProduct", oProduct);
    }
    
    public void deleteOProduct(OrderProductDTO oProduct){
        sqlSession.delete(PREFIX+"deleteOProduct", oProduct);
    }
    
    public void updateOProduct(OrderProductDTO oProduct){
        sqlSession.update(PREFIX+"updateOProduct", oProduct);
    }
    
}