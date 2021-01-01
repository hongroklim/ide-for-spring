package dev.rokong.user;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.rokong.dto.UserDTO;

@RestController
@RequestMapping(value="/user", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags={"User"})
public class UserController {
    
    @Autowired
    private UserService userService;

    @RequestMapping(value="", method=RequestMethod.GET)
    public List<UserDTO> getUsers(){
        return userService.getUsers();
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    public UserDTO createUser(@RequestBody UserDTO user) {
        return userService.createUser(user);
    }

    @RequestMapping(value="/{userNm}", method=RequestMethod.GET)
    public UserDTO getUser(@PathVariable String userNm){
        return userService.getUser(userNm);
    }

    @RequestMapping(value="/{userNm}/pwd", method=RequestMethod.PUT)
    public UserDTO updateUserPassword(@RequestBody UserDTO user){
        return userService.updateUser(user);
    }

    @RequestMapping(value="/{userNm}/enabled", method=RequestMethod.PUT)
    public UserDTO updateUserEnabled(@RequestBody UserDTO user){
        return userService.updateUser(user);
    }

    @RequestMapping(value="/{userNm}/authority", method=RequestMethod.PUT)
    
    public List<String> addUserAuthority(@RequestBody UserDTO user){
        List<GrantedAuthority> authorities = userService.addUserAuthorities(user);
        
        List<String> result = new ArrayList<>();
        for(GrantedAuthority a : authorities){
            result.add(a.getAuthority());
        }
        return result;
    }

    @RequestMapping(value="/{userNm}/authority", method=RequestMethod.DELETE)
    
    public void deleteUserAuthority(@RequestBody UserDTO user){
        userService.deleteUserAuthorities(user);
    }

    @RequestMapping(value="/{userNm}", method=RequestMethod.DELETE)
    
    public void deleteUser(@PathVariable String userNm){
        userService.deleteUser(userNm);
    }
}