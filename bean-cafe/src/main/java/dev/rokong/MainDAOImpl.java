package dev.rokong;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MainDAOImpl implements MainDAO {

    @Autowired SqlSessionTemplate sqlSession;

    public String selectCurrentDate() {
        return sqlSession.selectOne("dev.rokong.MainMapper.selectCurrentDate");
    }
    
}