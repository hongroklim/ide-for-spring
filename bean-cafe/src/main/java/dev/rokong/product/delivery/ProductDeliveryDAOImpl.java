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

    public ProductDeliveryDTO selectPDelivery(int id){
        return sqlSession.selectOne(PREFIX+"selectPDelivery", id);
    }

    public List<ProductDeliveryDTO> selectPDeliveryBySeller(String sellerNm){
        return sqlSession.selectList(PREFIX+"selectPDeliveryBySellerNm", sellerNm);
    }

    public ProductDeliveryDTO selectPDeliveryByProductId(int productId){
        return sqlSession.selectOne(PREFIX+"selectPDeliveryByProductId", productId);
    }

    public int insertPDelivery(ProductDeliveryDTO pDelivery){
        sqlSession.insert(PREFIX+"insertPDelivery", pDelivery);
        return pDelivery.getId();
    }

    public void updatePDelivery(ProductDeliveryDTO pDelivery){
        sqlSession.update(PREFIX+"updatePDelivery", pDelivery);
    }

    public void deletePDelivery(int id){
        sqlSession.delete(PREFIX+"deletePDelivery", id);
    }
}