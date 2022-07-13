package user;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import config.SpringConfig;
import com.company.dto.UserDTO;
import com.company.user.UserService;

public class ServiceTest extends SpringConfig {
    
    @Autowired UserService userService;

    @Test
    public void preventDuplicateAuth(){
        //create new user
        UserDTO newUser = new UserDTO();
        newUser.setUserNm("test01");
        newUser.setPwd("test01");
        newUser.setEnabled(true);
        userService.createUser(newUser);

        //authority list to be added
        List<String> authList = new ArrayList<>();
        authList.add("role01");
        authList.add("role02");
        newUser.setAuthority(authList);
        userService.addUserAuthorities(newUser);

        //additional authorities with duplicate (role02)
        List<String> authList2 = new ArrayList<>();
        authList2.add("role02");
        authList2.add("role03");
        newUser.setAuthority(authList2);
        userService.addUserAuthorities(newUser);

        List<GrantedAuthority> tobeList = new ArrayList<>();
        tobeList.add(new SimpleGrantedAuthority("role01"));
        tobeList.add(new SimpleGrantedAuthority("role02"));
        tobeList.add(new SimpleGrantedAuthority("role03"));

        List<GrantedAuthority> getAuthList = userService.getUserAuthorities(newUser);
        assertThat(getAuthList, is(notNullValue()));
        assertThat(getAuthList.size(), is(equalTo(3)));
        assertThat(getAuthList, containsInAnyOrder(tobeList.toArray()));
    }

    @Test
    public void deleteUserAndAuth(){
        UserDTO newUser = new UserDTO();
        newUser.setUserNm("test01");
        newUser.setPwd("test01");
        newUser.setEnabled(true);
        userService.createUser(newUser);

        //authority list to be added
        List<String> authList = new ArrayList<>();
        authList.add("role01");
        authList.add("role02");
        newUser.setAuthority(authList);
        userService.addUserAuthorities(newUser);

        //whether user exists
        UserDTO getUser = userService.getUser(newUser.getUserNm());
        assertThat(getUser, is(notNullValue()));
        assertThat(getUser.getUserNm(), is(equalTo(newUser.getUserNm())));

        //whether user's authorities exists
        List<GrantedAuthority> getAuthList = userService.getUserAuthorities(newUser);
        assertThat(getAuthList, is(not(empty())));

        //delete user
        userService.deleteUser(newUser.getUserNm());

        //user is not exists
        getUser = userService.getUser(newUser.getUserNm());
        assertThat(getUser, is(nullValue()));

        //user authorities are also deleted
        getAuthList = userService.getUserAuthorities(newUser);
        assertThat(getAuthList, is(notNullValue()));
        assertThat(getAuthList, is(empty()));
    }
}
