package dev.rokong.main;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MainDAOImpl implements MainDAO {

    public static final String PREFIX = "dev.rokong.main.";

    @Autowired SqlSessionTemplate sqlSession;

    public String selectCurrentDate() {
        return sqlSession.selectOne(PREFIX+"selectCurrentDate");
    }

    public void resetSerial(){
        sqlSession.selectOne(PREFIX+"resetSerial");
    }
    
}