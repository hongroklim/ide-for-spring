package user;

import static org.junit.Assert.*;
import static org.hamcrest.core.IsEqual.equalTo;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import config.SpringConfig;
import dev.rokong.dto.UserDTO;
import dev.rokong.user.UserDAO;
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

}