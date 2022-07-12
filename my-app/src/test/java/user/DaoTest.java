package user;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import config.SpringConfig;
import com.company.dto.UserDTO;
import com.company.user.UserDAO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoTest extends SpringConfig {

    @Autowired UserDAO userDAO;

    @Test
    public void selectUserListAndUser(){
        List<UserDTO> userList = userDAO.selectUserList();
        log.debug(userList.toString());
        
        if(userList!=null && userList.size()>0){
            UserDTO user1 = userList.get(0);
            UserDTO user2 = userDAO.selectUser(user1.getUserNm());
    
            assertThat(user1, equalTo(user2));
        }
    }

    @Test
    public void createUser(){
        List<UserDTO> prevList = userDAO.selectUserList();
        
        int prevSize = 0;
        if(prevList!=null && prevList.size()>0){
            prevSize = prevList.size();
        }

        UserDTO newUser = new UserDTO();
        newUser.setUserNm("testUser");
        newUser.setPwd("testUser");
        newUser.setEnabled(true);

        userDAO.insertUser(newUser);

        List<UserDTO> nextList = userDAO.selectUserList();

        assertThat(nextList.size(), equalTo(prevSize+1));
    }

    @Test(expected=DuplicateKeyException.class)
    public void createUserTriggerPKConstraint(){
        UserDTO newUser = new UserDTO();
        newUser.setUserNm("testUser");
        newUser.setPwd("testUser");
        newUser.setEnabled(true);

        userDAO.insertUser(newUser);

        assertThat(userDAO.selectUser(newUser.getUserNm()),
            equalTo(newUser));

        userDAO.insertUser(newUser);    //DuplicateKeyException!
    }

    @Test(expected=DataIntegrityViolationException.class)
    public void createUserWithNullPassword(){
        UserDTO newUser = new UserDTO();
        newUser.setUserNm("testUser");
        newUser.setPwd(null);
        newUser.setEnabled(true);

        userDAO.insertUser(newUser);
    }

    @Test(expected=DataIntegrityViolationException.class)
    public void createUserWithOverflow(){
        String userNm = "asdfasdfqwerqwersvzxcvzxcasdfarqwerqwerqwerxcvzxcvz";
        
        assertThat(userNm.length(), greaterThan(50));

        UserDTO newUser = new UserDTO();
        newUser.setUserNm(userNm);
        newUser.setPwd("testUser");
        newUser.setEnabled(true);

        userDAO.insertUser(newUser);
    }

    @Test
    public void insertUserAuth(){
        List<UserDTO> userList = userDAO.selectUserList();

        assertThat(userList, is(notNullValue()));
        assertThat(userList.size(), is(greaterThan(1)));

        UserDTO insertUser = userList.get(1);
        List<String> authList = new ArrayList<>();
        authList.add("seller");
        authList.add("buyer");
        insertUser.setAuthority(authList);

        userDAO.insertUserAuthorities(insertUser);

        UserDTO getUser = userDAO.selectUser(insertUser.getUserNm());
        assertThat(getUser, is(notNullValue()));

        List<String> getAuthList = userDAO.selectUserAuthorities(getUser.getUserNm());
        assertThat(getAuthList, is(notNullValue()));
        log.debug(getAuthList.toString());
        assertThat(getAuthList, is(equalTo(authList)));
    }

}