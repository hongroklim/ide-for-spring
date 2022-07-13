package com.company.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.dto.UserDTO;
import com.company.user.UserService;
import com.company.util.RandomUtil;

@Component("MockUser")
public class MockUser extends AbstractMockObject<UserDTO> {

    @Autowired UserService uService;

    @Override
    public UserDTO temp() {
        UserDTO user = new UserDTO();

        user.setUserNm("user"+RandomUtil.randomString(4));
        user.setPwd("pwd"+RandomUtil.randomString(10));
        user.setEnabled(true);

        return user;
    }

    @Override
    protected UserDTO createObjService(UserDTO obj) {
        return uService.createUser(obj);
    }

    @Override
    protected UserDTO getObjService(UserDTO obj) {
        return uService.getUser(obj.getUserNm());
    }
}