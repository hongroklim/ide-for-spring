package dev.rokong.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.rokong.dto.UserDTO;
import dev.rokong.exception.ApplicationException;
import dev.rokong.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired UserDAO userDAO;

    public List<UserDTO> getUsers() {
        log.debug("get user list");
        return userDAO.selectUserList();
    }

    public UserDTO getUser(String userNm) {
        return userDAO.selectUser(userNm);
    }

    public UserDTO createUser(UserDTO user) {
        try{
            userDAO.insertUser(user);
        }catch(DuplicateKeyException e){
            log.warn(user.getUserNm()+"already exists");
            throw new BusinessException("", e);
        }catch(DataAccessException e){
            log.error(e.getMessage());
            throw new ApplicationException("", e);
        }
        
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