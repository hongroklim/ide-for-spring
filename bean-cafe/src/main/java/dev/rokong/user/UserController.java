package dev.rokong.user;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.rokong.dto.UserDTO;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired UserService userService;

    @RequestMapping(value="", method=RequestMethod.GET)
    public List<UserDTO> getUsers(){
        return userService.getUsers();
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public UserDTO createUser(@RequestBody UserDTO user) {
        return userService.createUser(user);
    }

    @RequestMapping(value="/{userNm}", method=RequestMethod.GET)
    public UserDTO getUser(@PathVariable String userNm){
        return userService.getUser(userNm);
    }

    @RequestMapping(value="/{userNm}/pwd", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateUserPassword(@RequestBody UserDTO user){
        return userService.updateUserPassword(user);
    }

    @RequestMapping(value="/{userNm}/enabled", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateUserEnabled(@RequestBody UserDTO user){
        return userService.updateUserEnabled(user);
    }

    

    @RequestMapping(value="/{userNm}/authority", method=RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.OK)
    public Collection<? extends GrantedAuthority> addUserAuthority(@RequestBody UserDTO user){
        return userService.addUserAuthorities(user);
    }

    @RequestMapping(value="/{userNm}", method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable String userNm){
        userService.deleteUser(userNm);
    }
}