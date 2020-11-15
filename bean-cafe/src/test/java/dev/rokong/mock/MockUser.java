package dev.rokong.mock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.rokong.dto.UserDTO;
import dev.rokong.user.UserService;

@Component("MockUser")
public class MockUser extends MockObjects {

    private List<UserDTO> userList = new ArrayList<UserDTO>();

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

    private boolean isValidList(){
        if(this.userList.size() == 0){
            return true;
        }else{
            return uService.getUser(this.userList.get(0).getUserNm()) != null;
        }
    }

    private void validatingList(){
        if(!this.isValidList()){
            this.userList.clear();
        }
    }

    public UserDTO anyUser(){
        this.validatingList();

        if(this.userList.size() == 0){
            this.userList.add(this.createUser());
        }
        return this.userList.get(0);
    }

    public List<UserDTO> anyUserList(int count){
        this.validatingList();

        while(this.userList.size() < count){
            this.userList.add(this.createUser());
        }
        return this.userList.subList(0, count);
    }
}