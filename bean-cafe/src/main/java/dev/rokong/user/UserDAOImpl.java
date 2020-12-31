package dev.rokong.user;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.rokong.dto.UserDTO;

@Repository
public class UserDAOImpl implements UserDAO {
    public static final String PREFIX = "dev.rokong.user.";

    @Autowired SqlSessionTemplate sqlSession;

    public List<UserDTO> selectList(){
        return sqlSession.selectList(PREFIX+"selectList");
    }

    public UserDTO select(String userNm) {
        return sqlSession.selectOne(PREFIX+"select", userNm);
    }

    public int count(String userNm){
        return sqlSession.selectOne(PREFIX+"count", userNm);
    }

    public void insert(UserDTO user) {
        sqlSession.insert(PREFIX+"insert", user);
    }

    public void update(UserDTO user){
        sqlSession.update(PREFIX+"update", user);
    }

    public void delete(String userNm) {
        sqlSession.delete(PREFIX+"delete", userNm);
    }

    public void insertAuths(UserDTO user){
        sqlSession.insert(PREFIX+"insertAuths", user);
    }
    
    public List<String> selectAuths(String userNm){
        return sqlSession.selectList(PREFIX+"selectAuths", userNm);
    }

    public void deleteAuths(UserDTO user){
        sqlSession.delete(PREFIX+"deleteAuths", user);
    }
}