package com.company.product.option;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.company.dto.ProductOptionDTO;

@Repository
public class ProductOptionDAOImpl implements ProductOptionDAO {
    
    public static final String PREFIX = "com.company.productOption.";

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

    public void updateProductOption(ProductOptionDTO asisPOption, String name, int ord){
        Map<String, Object> hm = new HashMap<String, Object>();
        hm.put("pOption", asisPOption);
        hm.put("name", name);
        hm.put("ord", ord);

        sqlSession.update(PREFIX+"updateProductOption", hm);
    }

    public void backwardOptionOrder(ProductOptionDTO pOption, int startOrder, int endOrder){
        Map<String, Object> hm = new HashMap<String, Object>();
        hm.put("pOption", pOption);
        hm.put("startOrder", startOrder);
        hm.put("endOrder", endOrder);
        sqlSession.update(PREFIX+"backwardOptionOrder", hm);
    }

    public void forwardOptionOrder(ProductOptionDTO pOption, int startOrder, int endOrder){
        Map<String, Object> hm = new HashMap<String, Object>();
        hm.put("pOption", pOption);
        hm.put("startOrder", startOrder);
        hm.put("endOrder", endOrder);
        sqlSession.update(PREFIX+"forwardOptionOrder", hm);
    }

    public void updateOptionGroup(ProductOptionDTO pOption, int tobeGroup){
        Map<String, Object> hm = new HashMap<String, Object>();
        hm.put("pOption", pOption);
        hm.put("tobeGroup", tobeGroup);
        sqlSession.update(PREFIX+"updateOptionGroup", hm);
    }

    public void backwardOptionGroup(int productId, int startGroup, int endGroup){
        Map<String, Integer> hm = new HashMap<String, Integer>();
        hm.put("productId", productId);
        hm.put("startGroup", startGroup);
        hm.put("endGroup", endGroup);
        sqlSession.update(PREFIX+"backwardOptionGroup", hm);
    }

    public void forwardOptionGroup(int productId, int startGroup, int endGroup){
        Map<String, Integer> hm = new HashMap<String, Integer>();
        hm.put("productId", productId);
        hm.put("startGroup", startGroup);
        hm.put("endGroup", endGroup);
        sqlSession.update(PREFIX+"forwardOptionGroup", hm);
    }
}
