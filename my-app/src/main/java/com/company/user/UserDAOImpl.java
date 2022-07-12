package com.company.user;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.company.dto.UserDTO;

@Repository
public class UserDAOImpl implements UserDAO {
    public static final String PREFIX = "com.company.user.";

    @Autowired SqlSessionTemplate sqlSession;

    public List<UserDTO> selectUserList(){
        return sqlSession.selectList(PREFIX+"selectUserList");
    }

    public UserDTO selectUser(String userNm) {
        return sqlSession.selectOne(PREFIX+"selectUser", userNm);
    }

    public void insertUser(UserDTO user) {
        sqlSession.insert(PREFIX+"insertUser", user);
    }

    public void updateUserPassword(UserDTO user) {
        sqlSession.update(PREFIX+"updateUserPassword", user);
    }

    public void updateUserEnabled(UserDTO user) {
        sqlSession.update(PREFIX+"updateUserEnabled", user);
    }

    public void deleteUser(String userNm) {
        sqlSession.delete(PREFIX+"deleteUser", userNm);
    }

    public void insertUserAuthorities(UserDTO user){
        sqlSession.insert(PREFIX+"insertUserAuthorities", user);
    }
    
    public List<String> selectUserAuthorities(String userNm){
        return sqlSession.selectList(PREFIX+"selectUserAuthorities", userNm);
    }

    public void deleteUserAuthorities(UserDTO user){
        sqlSession.delete(PREFIX+"deleteUserAuthorities", user);
    }
}