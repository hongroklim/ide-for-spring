package dev.rokong.product.option;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void insertProductOption(ProductOptionDTO pOption){
        sqlSession.insert(PREFIX+"insertProductOption", pOption);
    }

    public void deleteProductOption(ProductOptionDTO pOption){
        sqlSession.delete(PREFIX+"deleteProductOption", pOption);
    }

    public void updateProductOption(ProductOptionDTO asisPOption, String optionId, String name){
        Map<String, Object> hm = new HashMap<String, Object>();
        hm.put("pOption", asisPOption);
        hm.put("optionId", optionId);
        hm.put("name", name);

        sqlSession.update(PREFIX+"updateProductOption", hm);
    }

    public void backwardOptionOrder(ProductOptionDTO pOption, String startId, String endId){
        Map<String, Object> hm = new HashMap<String, Object>();
        hm.put("pOption", pOption);
        hm.put("startId", startId);
        hm.put("endId", endId);
        sqlSession.update(PREFIX+"backwardOptionOrder", hm);
    }

    public void forwardOptionOrder(ProductOptionDTO pOption, String startId, String endId){
        Map<String, Object> hm = new HashMap<String, Object>();
        hm.put("pOption", pOption);
        hm.put("startId", startId);
        hm.put("endId", endId);
        sqlSession.update(PREFIX+"forwardOptionOrder", hm);
    }
}
