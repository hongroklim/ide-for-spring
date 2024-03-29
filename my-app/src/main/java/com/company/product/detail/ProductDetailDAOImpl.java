package com.company.product.detail;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.company.dto.ProductDetailDTO;

@Repository
public class ProductDetailDAOImpl implements ProductDetailDAO {
    
    public static final String PREFIX = "com.company.productDetail.";

    @Autowired SqlSessionTemplate sqlSession;

    public List<ProductDetailDTO> selectDetailList(ProductDetailDTO pDetail){
        return sqlSession.selectList(PREFIX+"selectDetailList", pDetail);
    }

    public ProductDetailDTO selectDetail(ProductDetailDTO pDetail){
        return sqlSession.selectOne(PREFIX+"selectDetail", pDetail);
    }
    
    public void insertDetail(ProductDetailDTO pDetail){
        sqlSession.insert(PREFIX+"insertDetail", pDetail);
    }
    
    public void deleteDetail(ProductDetailDTO pDetail){
        sqlSession.delete(PREFIX+"deleteDetail", pDetail);
    }

    public void deleteDetailList(ProductDetailDTO pDetail){
        sqlSession.delete(PREFIX+"deleteDetailList", pDetail);
    }
    
    public void updateDetail(ProductDetailDTO pDetail){
        sqlSession.update(PREFIX+"updateDetail", pDetail);
    }
    
}