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

    public List<ProductOptionDTO> selectList(ProductOptionDTO pOption){
        return sqlSession.selectList(PREFIX+"select", pOption);
    }
    
    public ProductOptionDTO select(ProductOptionDTO pOption){
        return sqlSession.selectOne(PREFIX+"select", pOption);
    }

    public void insert(ProductOptionDTO pOption){
        sqlSession.insert(PREFIX+"insert", pOption);
    }

    public void delete(ProductOptionDTO pOption){
        sqlSession.delete(PREFIX+"delete", pOption);
    }

    public void update(ProductOptionDTO pOption){
        sqlSession.update(PREFIX+"update", pOption);
    }

    public void backwardOptionOrder(ProductOptionDTO pOption, int startOrder, int endOrder){
        Map<String, Object> hm = new HashMap<>();
        hm.put("pOption", pOption);
        hm.put("startOrder", startOrder);
        hm.put("endOrder", endOrder);
        sqlSession.update(PREFIX+"backwardOptionOrder", hm);
    }

    public void forwardOptionOrder(ProductOptionDTO pOption, int startOrder, int endOrder){
        Map<String, Object> hm = new HashMap<>();
        hm.put("pOption", pOption);
        hm.put("startOrder", startOrder);
        hm.put("endOrder", endOrder);
        sqlSession.update(PREFIX+"forwardOptionOrder", hm);
    }

    public void updateOptionGroup(ProductOptionDTO pOption, int tobeGroup){
        Map<String, Object> hm = new HashMap<>();
        hm.put("pOption", pOption);
        hm.put("tobeGroup", tobeGroup);
        sqlSession.update(PREFIX+"updateOptionGroup", hm);
    }

    public void backwardOptionGroup(int productId, int startGroup, int endGroup){
        Map<String, Integer> hm = new HashMap<>();
        hm.put("productId", productId);
        hm.put("startGroup", startGroup);
        hm.put("endGroup", endGroup);
        sqlSession.update(PREFIX+"backwardOptionGroup", hm);
    }

    public void forwardOptionGroup(int productId, int startGroup, int endGroup){
        Map<String, Integer> hm = new HashMap<>();
        hm.put("productId", productId);
        hm.put("startGroup", startGroup);
        hm.put("endGroup", endGroup);
        sqlSession.update(PREFIX+"forwardOptionGroup", hm);
    }
}
