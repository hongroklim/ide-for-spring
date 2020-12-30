package dev.rokong.product.delivery;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.rokong.dto.ProductDeliveryDTO;

@Repository
public class ProductDeliveryDAOImpl implements ProductDeliveryDAO {

    @Autowired SqlSession sqlSession;

    public static final String PREFIX = "dev.rokong.productDelivery.";

    public ProductDeliveryDTO select(int id){
        return sqlSession.selectOne(PREFIX+"select", id);
    }

    public int count(int id){
        return sqlSession.selectOne(PREFIX+"count", id);
    }

    public int insert(ProductDeliveryDTO pDelivery){
        sqlSession.insert(PREFIX+"insert", pDelivery);
        return pDelivery.getId();
    }

    public void update(ProductDeliveryDTO pDelivery){
        sqlSession.update(PREFIX+"update", pDelivery);
    }

    public void delete(int id){
        sqlSession.delete(PREFIX+"delete", id);
    }
}