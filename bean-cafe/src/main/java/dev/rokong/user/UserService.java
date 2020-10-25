package dev.rokong.user;

import java.util.List;

import dev.rokong.dto.UserDTO;

public interface UserService {
    public List<UserDTO> getUsers();
    public UserDTO getUser(String userNm);
    public UserDTO createUser(UserDTO user);
    public UserDTO updateUser(UserDTO user);
    public void deleteUser(String userNm);
}