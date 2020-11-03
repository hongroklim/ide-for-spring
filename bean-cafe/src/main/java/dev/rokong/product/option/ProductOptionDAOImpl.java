package dev.rokong.product.option;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.rokong.dto.ProductOptionDTO;

@Repository
public class ProductOptionDAOImpl implements ProductOptionDAO {
    
    public static final String PREFIX = "dev.rokong.productOption.";

    @Autowired SqlSessionTemplate sqlSession;

    public List<ProductOptionDTO> selectProductOptionList(ProductOptionDTO pOption){
        return sqlSession.selectList(PREFIX+"selectProductOption", pOption);
    }
    
    public ProductOptionDTO selectProductOption(ProductOptionDTO pOption){
        return sqlSession.selectOne(PREFIX+"selectProductOption", pOption);
    }
}
