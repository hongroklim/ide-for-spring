package dev.rokong.cart;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CartDAOImpl implements CartDAO {
    
    public static final String PREFIX = "dev.rokong.cart.";

    @Autowired SqlSessionTemplate sqlSession;

    
}