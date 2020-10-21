package dev.rokong.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.rokong.dto.UserDTO;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired UserDAO userDAO;

    public List<UserDTO> getUsers() {
        logger.debug("return user list");
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