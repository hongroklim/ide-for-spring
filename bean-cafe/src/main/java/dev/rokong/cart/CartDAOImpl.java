package dev.rokong.cart;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.rokong.dto.CartDTO;

@Repository
public class CartDAOImpl implements CartDAO {
    
    public static final String PREFIX = "dev.rokong.cart.";

    @Autowired SqlSessionTemplate sqlSession;

    public List<CartDTO> selectCarts(CartDTO cart){
        return sqlSession.selectList(PREFIX+"selectCart", cart);
    }

    public CartDTO selectCart(CartDTO cart){
        return sqlSession.selectOne(PREFIX+"selectCart", cart);
    }
    
    public void insertCart(CartDTO cart){
        sqlSession.insert(PREFIX+"insertCart", cart);
    }
    
    public void deleteCart(CartDTO cart){
        sqlSession.delete(PREFIX+"deleteCart", cart);
    }
    
    public void updateCartCnt(CartDTO cart){
        sqlSession.update("updateCartCnt", cart);
    }
}