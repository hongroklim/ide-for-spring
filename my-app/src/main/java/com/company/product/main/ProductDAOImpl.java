package com.company.product.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.company.dto.ProductDTO;

@Repository
public class ProductDAOImpl implements ProductDAO {

    public static final String PREFIX = "com.company.product.";

    @Autowired SqlSessionTemplate sqlSession;

    public List<ProductDTO> selectProductList() {
        return sqlSession.selectList(PREFIX+"selectProductList");
    }

    public ProductDTO selectProduct(int id) {
        return sqlSession.selectOne(PREFIX+"selectProduct", id);
    }

    public List<ProductDTO> selectProductsByDelivery(int deliveryId){
        return sqlSession.selectList(PREFIX+"selectProductsByDelivery", deliveryId);
    }

    public int insertProduct(ProductDTO product) {
        sqlSession.insert(PREFIX+"insertProduct", product);
        return product.getId();
    }

    public void deleteProduct(int id) {
        sqlSession.delete(PREFIX+"deleteProduct", id);
    }

    public void updateProduct(ProductDTO product) {
        sqlSession.update(PREFIX+"updateProduct", product);
    }

    public void updateProductColumn(int id, String column, Object value) {
        Map<String, Object> hm = new HashMap<String, Object>();
        hm.put("id", id);
        hm.put("column", column);
        hm.put("value", value);

        sqlSession.update(PREFIX+"updateProductColumn", hm);
    }

    public void updateProductCategory(int asisCategory, int tobeCategory){
        Map<String, Integer> hm = new HashMap<String, Integer>();
        hm.put("asisCategory", asisCategory);
        hm.put("tobeCategory", tobeCategory);

        sqlSession.update(PREFIX+"updateProductCategory", hm);
    }
}
