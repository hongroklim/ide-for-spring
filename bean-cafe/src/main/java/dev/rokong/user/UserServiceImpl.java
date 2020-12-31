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
import dev.rokong.util.ObjUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    public List<UserDTO> getUsers() {
        return userDAO.selectList();
    }

    private void verifyUserNmDefined(String userNm){
        if(ObjUtil.isEmpty(userNm)){
            throw new IllegalArgumentException("user name is not defined");
        }
    }

    private void verifyUserNmDefined(UserDTO user){
        if(user == null){
            throw new IllegalArgumentException("user is not defined");
        }
        this.verifyUserNmDefined(user.getUserNm());
    }

    public UserDTO getUser(String userNm) {
        this.verifyUserNmDefined(userNm);
        return userDAO.select(userNm);
    }

    public UserDTO getUserNotNull(String userNm){
        UserDTO user = this.getUser(userNm);
        if(user == null){
            throw new BusinessException(userNm+" user is not exists");
        }
        return user;
    }

    private UserDTO getUserNotNull(UserDTO user){
        this.verifyUserNmDefined(user);
        return this.getUserNotNull(user.getUserNm());
    }

    public UserDTO createUser(UserDTO user) {
        UserDTO getUser = this.getUser(user.getUserNm());
        
        if(getUser != null){
            throw new BusinessException(user.getUserNm()+" user is already exists");
        }

        //insert user
        userDAO.insert(user);

        return this.getUser(user.getUserNm());
    }

    public UserDTO updateUser(UserDTO user){
        this.checkUserExist(user.getUserNm());

        if(ObjUtil.isEmpty(user.getPwd()) && user.getEnabled() == null){
            //if nothing to change, return
            return this.getUserNotNull(user);
        }

        //update
        userDAO.update(user);

        return this.getUserNotNull(user);
    }

    public void deleteUser(String userNm) {
        this.checkUserExist(userNm);
        userDAO.delete(userNm);
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

        userDAO.insertAuths(user);
        return this.getUserAuthorities(user);
    }

    public void checkUserExist(String userNm){
        this.verifyUserNmDefined(userNm);

        if (userDAO.count(userNm) == 0) {
            throw new BusinessException(userNm + " user is not exists");
        }
    }

    private UserDTO getUserWithAuthorities(UserDTO user){
        UserDTO getUser = this.getUserNotNull(user);
        List<GrantedAuthority> authorities = this.getUserAuthorities(user);
        getUser.setAuthorities(authorities);
        return getUser;
    }

    public List<GrantedAuthority> getUserAuthorities(final UserDTO user){
        List<String> authList = userDAO.selectAuths(user.getUserNm());
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

        userDAO.deleteAuths(user);
    }
}