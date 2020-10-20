package dev.rokong.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.rokong.dto.UserDTO;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired UserService userService;

    @RequestMapping(value="/", method=RequestMethod.GET)
    public List<UserDTO> getUsers(){
        return userService.getUsers();
    }

    @RequestMapping(value="/", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public UserDTO createUser(@ModelAttribute UserDTO user){
        return userService.createUser(user);
    }

    @RequestMapping(value="/{userNm}", method=RequestMethod.GET)
    public UserDTO getUser(@PathVariable String userNm){
        return userService.getUser(userNm);
    }

    @RequestMapping(value="/{userNm}", method=RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateUser(@ModelAttribute UserDTO user){
        return userService.updateUser(user);
    }

    @RequestMapping(value="/{userNm}", method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable String userNm){
        userService.deleteUser(userNm);
    }
}