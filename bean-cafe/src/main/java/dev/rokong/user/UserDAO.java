package dev.rokong.user;

import java.util.List;

import dev.rokong.dto.UserDTO;

public interface UserDAO {
    public List<UserDTO> selectList();
    public UserDTO select(String userNm);
    public int count(String userNm);
    public void insert(UserDTO user);
    public void update(UserDTO user);
    public void delete(String userNm);

    public void insertAuths(UserDTO user);
    public List<String> selectAuths(String userNm);
    public void deleteAuths(UserDTO user);
}