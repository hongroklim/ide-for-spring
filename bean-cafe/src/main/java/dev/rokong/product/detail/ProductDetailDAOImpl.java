package dev.rokong.product.detail;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.rokong.dto.ProductDetailDTO;

@Repository
public class ProductDetailDAOImpl implements ProductDetailDAO {
    
    public static final String PREFIX = "dev.rokong.productDetail.";

    @Autowired SqlSessionTemplate sqlSession;

    public List<ProductDetailDTO> selectList(ProductDetailDTO pDetail){
        return sqlSession.selectList(PREFIX+"selectList", pDetail);
    }

    public ProductDetailDTO select(ProductDetailDTO pDetail){
        return sqlSession.selectOne(PREFIX+"select", pDetail);
    }

    public int count(ProductDetailDTO pDetail){
        return sqlSession.selectOne(PREFIX + "count", pDetail);
    }
    
    public void insert(ProductDetailDTO pDetail){
        sqlSession.insert(PREFIX+"insert", pDetail);
    }
    
    public void delete(ProductDetailDTO pDetail){
        sqlSession.delete(PREFIX+"delete", pDetail);
    }

    public void deleteList(ProductDetailDTO pDetail){
        sqlSession.delete(PREFIX+"deleteList", pDetail);
    }
    
    public void update(ProductDetailDTO pDetail){
        sqlSession.update(PREFIX+"update", pDetail);
    }
    
}