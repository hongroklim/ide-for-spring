package dev.rokong.mock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.rokong.dto.UserDTO;
import dev.rokong.user.UserService;

@Component
public class MockUser extends MockObjects {

    private List<UserDTO> anyUserList = new ArrayList<UserDTO>();

    @Autowired UserService uService;

    public UserDTO tempUser(){
        UserDTO user = new UserDTO();
        user.setUserNm("user"+this.randomString(4));
        user.setPwd("pwd"+this.randomString(10));
        user.setEnabled(true);
        return user;
    }

    private UserDTO createUser(){
        return uService.createUser(this.tempUser());
    }

    public UserDTO anyUser(){
        if(this.anyUserList.size() == 0){
            this.anyUserList.add(this.createUser());
        }
        return this.anyUserList.get(0);
    }

    public List<UserDTO> anyUserList(int count){
        while(this.anyUserList.size() < count){
            this.anyUserList.add(this.createUser());
        }
        return this.anyUserList.subList(0, count);
    }
}