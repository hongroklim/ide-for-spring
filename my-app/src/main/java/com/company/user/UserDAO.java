package com.company.user;

import java.util.List;

import com.company.dto.UserDTO;

public interface UserDAO {
    public List<UserDTO> selectUserList();
    public UserDTO selectUser(String userNm);
    public void insertUser(UserDTO user);
    public void updateUserPassword(UserDTO user);
    public void updateUserEnabled(UserDTO user);
    public void deleteUser(String userNm);
    public void insertUserAuthorities(UserDTO user);
    public List<String> selectUserAuthorities(String userNm);
    public void deleteUserAuthorities(UserDTO user);
}