package dev.rokong.order.main;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDAOImpl implements OrderDAO {
    
    public static final String PREFIX = "dev.rokong.order.";

    @Autowired SqlSessionTemplate sqlSession;
    
}
