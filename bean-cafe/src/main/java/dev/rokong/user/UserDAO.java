package dev.rokong.user;

import java.util.List;

import dev.rokong.dto.UserDTO;

public interface UserDAO {
    public List<UserDTO> selectUserList();
    public UserDTO selectUser(String userNm);
    public void insertUser(UserDTO user);
    public void updateUser(UserDTO user);
    public void deleteUser(String userNm);
}