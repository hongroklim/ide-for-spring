package dev.rokong.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.rokong.dto.UserDTO;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired UserDAO userDAO;

    public List<UserDTO> getUsers() {
        return userDAO.selectUserList();
    }

    public UserDTO getUser(String userNm) {
        return userDAO.selectUser(userNm);
    }

    public UserDTO createUser(UserDTO user) {
        userDAO.insertUser(user);
        return this.getUser(user.getUserNm());
    }

    public UserDTO updateUser(UserDTO user) {
        userDAO.updateUser(user);
        return this.getUser(user.getUserNm());
    }
    
    public void deleteUser(String userNm) {
        userDAO.deleteUser(userNm);
    }

}