package dev.rokong.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
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
        UserDTO getUser = userDAO.selectUser(user.getUserNm());
        
        if(getUser != null){
            log.warn(user.getUserNm()+"already exists");
            throw new BusinessException("");
        }

        try{
            userDAO.insertUser(user);
        }catch(DataAccessException e){
            log.error(e.getMessage());
            throw new ApplicationException("", e);
        }
        
        return this.getUser(user.getUserNm());
    }

    public UserDTO updateUserPassword(UserDTO user) {
        UserDTO getUser = this.getUserNotNull(user);
        userDAO.updateUserPassword(user);
        return this.getUser(getUser.getUserNm());
    }
    
    public UserDTO updateUserEnabled(UserDTO user) {
        UserDTO getUser = this.getUserNotNull(user);
        userDAO.updateUserEnabled(user);
        return this.getUser(getUser.getUserNm());
    }

    public void deleteUser(String userNm) {
        UserDTO getUser = this.getUserNotNull(userNm);
        userDAO.deleteUser(getUser.getUserNm());
    }

    public List<GrantedAuthority> addUserAuthorities(UserDTO user){
        UserDTO getUser = this.getUserWithAuthorities(user);

        //check authorities to be added whether already exists
        List<GrantedAuthority> addAuthorities = user.getAuthorities();
        for(GrantedAuthority auth : addAuthorities){
            if(getUser.getAuthorities().contains(auth)){
                //if the role already exists, remove it
                log.debug(auth.getAuthority()+"is already exists.");
                addAuthorities.remove(auth);
            }
        }
        user.setAuthorities(addAuthorities);

        userDAO.insertUserAuthorities(user);
        return this.getUserAuthorities(user);
    }

    public UserDTO getUserNotNull(String userNm){
        UserDTO user = new UserDTO();
        user.setUserNm(userNm);
        return this.getUserNotNull(user);
    }

    private UserDTO getUserNotNull(UserDTO user){
        UserDTO getUser = this.getUser(user.getUserNm());
        if(getUser == null){
            throw new BusinessException(user.getUserNm()+"is not exists");
        }
        return getUser;
    }

    private UserDTO getUserWithAuthorities(UserDTO user){
        UserDTO getUser = this.getUserNotNull(user);
        List<GrantedAuthority> authorities = this.getUserAuthorities(user);
        getUser.setAuthorities(authorities);
        return getUser;
    }

    public List<GrantedAuthority> getUserAuthorities(final UserDTO user){
        List<String> authList = userDAO.selectUserAuthorities(user.getUserNm());
        UserDTO tempUser = new UserDTO();
        tempUser.setAuthority(authList);
        return tempUser.getAuthorities();
    }

    public void deleteUserAuthorities(UserDTO user){
        UserDTO getUser = this.getUserWithAuthorities(user);

        //check authorities to be deleted
        List<GrantedAuthority> deleteAuthorities = user.getAuthorities();
        for(GrantedAuthority auth : deleteAuthorities){
            if(!getUser.getAuthorities().contains(auth)){
                //if the role already exists, remove it
                log.debug(auth.getAuthority()+"is not exists.");
                deleteAuthorities.remove(auth);
            }
        }
        user.setAuthorities(deleteAuthorities);

        userDAO.deleteUserAuthorities(user);
    }
}