package dev.rokong.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.rokong.dto.ProductDTO;

@Repository
public class ProductDAOImpl implements ProductDAO {

    public static final String PREFIX = "dev.rokong.ProductMapper.";

    @Autowired SqlSessionTemplate sqlSession;

    public List<ProductDTO> selectProductList() {
        return sqlSession.selectList(PREFIX+"selectProductList");
    }

    public ProductDTO selectProduct(int id) {
        return sqlSession.selectOne(PREFIX+"selectProduct", id);
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
}
