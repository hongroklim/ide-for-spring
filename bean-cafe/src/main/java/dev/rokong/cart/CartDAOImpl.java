package dev.rokong.cart;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.rokong.dto.CartDTO;

@Repository
public class CartDAOImpl implements CartDAO {
    
    public static final String PREFIX = "dev.rokong.cart.";

    @Autowired
    private SqlSessionTemplate sqlSession;

    public List<CartDTO> selectList(CartDTO cart){
        return sqlSession.selectList(PREFIX+"select", cart);
    }

    public CartDTO select(CartDTO cart){
        return sqlSession.selectOne(PREFIX+"select", cart);
    }

    public int count(CartDTO cart){
        return sqlSession.selectOne(PREFIX + "count", cart);
    }

    public void insert(CartDTO cart){
        sqlSession.insert(PREFIX+"insert", cart);
    }
    
    public void delete(CartDTO cart){
        sqlSession.delete(PREFIX+"delete", cart);
    }
    
    public void updateCnt(CartDTO cart){
        sqlSession.update(PREFIX+"updateCnt", cart);
    }
}