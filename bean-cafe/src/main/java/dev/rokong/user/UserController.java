package dev.rokong.user;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value="get user list", notes="")
    public List<UserDTO> getUsers(){
        return userService.getUsers();
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    @ApiOperation(value="create user", notes="")
    public UserDTO createUser(@RequestBody UserDTO user) {
        return userService.createUser(user);
    }

    @RequestMapping(value="/{userNm}", method=RequestMethod.GET)
    @ApiOperation(value="get user", notes="")
    public UserDTO getUser(@PathVariable String userNm){
        return userService.getUser(userNm);
    }

    @RequestMapping(value="/{userNm}/pwd", method=RequestMethod.PUT)
    @ApiOperation(value="update user password", notes="")
    public UserDTO updateUserPassword(@RequestBody UserDTO user){
        return userService.updateUser(user);
    }

    @RequestMapping(value="/{userNm}/enabled", method=RequestMethod.PUT)
    @ApiOperation(value="update user enabled", notes="")
    public UserDTO updateUserEnabled(@RequestBody UserDTO user){
        return userService.updateUser(user);
    }

    @RequestMapping(value="/{userNm}/authority", method=RequestMethod.PUT)
    @ApiOperation(value="add user authority", notes="")
    public List<String> addUserAuthority(@RequestBody UserDTO user){
        List<GrantedAuthority> authorities = userService.addUserAuthorities(user);
        
        List<String> result = new ArrayList<>();
        for(GrantedAuthority a : authorities){
            result.add(a.getAuthority());
        }
        return result;
    }

    @RequestMapping(value="/{userNm}/authority", method=RequestMethod.DELETE)
    @ApiOperation(value="delete user authority", notes="")
    public void deleteUserAuthority(@RequestBody UserDTO user){
        userService.deleteUserAuthorities(user);
    }

    @RequestMapping(value="/{userNm}", method=RequestMethod.DELETE)
    @ApiOperation(value="", notes="")
    public void deleteUser(@PathVariable String userNm){
        userService.deleteUser(userNm);
    }
}