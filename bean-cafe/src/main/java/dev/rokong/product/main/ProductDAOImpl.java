package dev.rokong.product.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.rokong.dto.ProductDTO;

@Repository
public class ProductDAOImpl implements ProductDAO {

    public static final String PREFIX = "dev.rokong.product.";

    @Autowired SqlSessionTemplate sqlSession;

    public List<ProductDTO> selectList() {
        return sqlSession.selectList(PREFIX+"select", null);
    }

    public ProductDTO select(int id) {
        return sqlSession.selectOne(PREFIX+"select", id);
    }

    public List<ProductDTO> selectByDelivery(int deliveryId){
        return sqlSession.selectList(PREFIX+"selectByDelivery", deliveryId);
    }

    public int count(int id){
        return sqlSession.selectOne(PREFIX+"count", id);
    }

    public int insert(ProductDTO product) {
        sqlSession.insert(PREFIX+"insert", product);
        return product.getId();
    }

    public void delete(int id) {
        sqlSession.delete(PREFIX+"delete", id);
    }

    public void update(ProductDTO product) {
        sqlSession.update(PREFIX+"update", product);
    }

    public void updateColumn(int id, String column, Object value) {
        Map<String, Object> hm = new HashMap<String, Object>();
        hm.put("id", id);
        hm.put("column", column);
        hm.put("value", value);

        sqlSession.update(PREFIX+"updateColumn", hm);
    }

    public void updateCategory(int asisCategory, int tobeCategory){
        Map<String, Integer> hm = new HashMap<>();
        hm.put("asisCategory", asisCategory);
        hm.put("tobeCategory", tobeCategory);

        sqlSession.update(PREFIX+"updateCategory", hm);
    }
}
