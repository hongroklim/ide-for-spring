package dev.rokong.user;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import dev.rokong.dto.UserDTO;

public interface UserService {
    public List<UserDTO> getUsers();
    public UserDTO getUser(String userNm);
    public UserDTO createUser(UserDTO user);
    public UserDTO updateUserPassword(UserDTO user);
    public UserDTO updateUserEnabled(UserDTO user);
    public void deleteUser(String userNm);
    public List<GrantedAuthority> getUserAuthorities(UserDTO user);
    public List<GrantedAuthority> addUserAuthorities(UserDTO user);
    public void deleteUserAuthorities(UserDTO user);
}