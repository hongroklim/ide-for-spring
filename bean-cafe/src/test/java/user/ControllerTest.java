package user;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import config.MvcUnitConfig;
import dev.rokong.dto.UserDTO;
import dev.rokong.user.UserController;
import dev.rokong.user.UserDAO;
import dev.rokong.user.UserService;
import dev.rokong.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControllerTest extends MvcUnitConfig {

    @Autowired UserController userController;
    @Autowired UserService userService;
    @Autowired UserDAO userDAO;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void initConfig() {
        assertThat(this.mvc, is(not(nullValue())));
    }

    @Test
    public void simpleRequest() throws Exception {
        this.mvc.perform(get("/user")).andDo(log()).andExpect(status().isOk());
    }

    @Test
    public void objectMapper() {
        assertThat(this.objectMapper, is(notNullValue()));
    }

    @Test
    public void MockObjectsTest(){
        RandomUtil.randomString();
        RandomUtil.randomString();
        mockObj.user.anyUser();
    }

    @Test
    public void createUser() throws Exception {
        UserDTO newUser = mockObj.user.tempUser();

        log.debug(this.objectMapper.writeValueAsString(newUser));

        this.mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(newUser))).andDo(log()).andExpect(status().isOk())
                .andExpect(jsonPath("$.userNm", is(equalTo(newUser.getUserNm()))))
                .andExpect(jsonPath("$.authority", is(nullValue())));
    }

    /**
     * request user list by MockMvc then parse JSON response as java.Object through
     * Jackson
     * 
     * @throws Exception
     */
    @Test
    public void parseJsonResponse() throws Exception {
        // request MockMvc then Return MvcResult by .andReturn()
        MvcResult result = this.mvc.perform(get("/user"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();

        // parse response content to string
        String content = result.getResponse().getContentAsString();

        // content returns array in jsonType
        assertThat(content, startsWith("[{"));
        assertThat(content, endsWith("}]"));
        List<UserDTO> userList = this.objectMapper.readValue(content, new TypeReference<List<UserDTO>>() {
        });

        // userList contains at least one user
        assertThat(userList, is(notNullValue()));
        assertThat(userList.size(), is(greaterThan(0)));
    }

    @Test
    public void updateUserPwd() throws Exception {
        UserDTO asisUser = mockObj.user.anyUser();

        UserDTO updateUser = new UserDTO(); // set new password
        updateUser.setUserNm(asisUser.getUserNm());
        updateUser.setPwd("newPassWord");

        this.mvc.perform(put("/user/" + asisUser.getUserNm() + "/pwd").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(updateUser))).andDo(log()).andExpect(status().isOk())
                .andExpect(jsonPath("$.userNm", is(equalTo(updateUser.getUserNm()))))
                .andExpect(jsonPath("$.pwd", is(not(equalTo(asisUser.getPwd()))))); // different pwd in same user
    }

    @Test
    public void updateUserEnabled() throws Exception {
        // create any user
        UserDTO asisUser = mockObj.user.anyUser();

        // make new user whose enabled is opposite compared to asis
        UserDTO tobeUser = new UserDTO();
        tobeUser.setUserNm(asisUser.getUserNm());
        tobeUser.setEnabled(!asisUser.isEnabled());

        this.mvc.perform(put("/user/" + asisUser.getUserNm() + "/enabled").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(tobeUser))).andDo(log()).andExpect(status().isOk())
                .andExpect(jsonPath("$.userNm", is(equalTo(tobeUser.getUserNm()))))
                .andExpect(jsonPath("$.enabled", is(not(equalTo(asisUser.isEnabled()))))); // different pwd in same user
    }

    @Test
    public void addUserAuthority() throws Exception {
        // create new user
        UserDTO newUser = mockObj.user.anyUser();

        // authority list to be added
        List<String> authList = new ArrayList<>();
        authList.add("role01");
        authList.add("role02");
        newUser.setAuthority(authList);

        MvcResult result = this.mvc.perform(patch("/user/" + newUser.getUserNm() + "/authority")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(this.objectMapper.writeValueAsString(newUser)))
                .andDo(log()).andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        List<String> authListResp = this.objectMapper.readValue(content, new TypeReference<List<String>>() {});

        //compare response with expected authority list
        assertThat(authListResp, is(not(nullValue())));
        assertThat(authListResp, containsInAnyOrder(authList.toArray()));
    }

    @Test
    public void deleteUser() throws Exception {
        UserDTO newUser = mockObj.user.anyUser();

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
        this.mvc.perform(delete("/user/"+newUser.getUserNm())
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(this.objectMapper.writeValueAsString(newUser)))
            .andDo(log())
            .andExpect(status().isOk());

        getUser = userService.getUser(newUser.getUserNm());
        assertThat(getUser, is(nullValue()));
    }

    @Test
    public void deleteUserAuthorities() throws Exception{
        // create new user
        UserDTO newUser = new UserDTO();
        newUser.setUserNm("test01");
        newUser.setPwd("test01");
        newUser.setEnabled(true);
        userService.createUser(newUser);

        //authority list to be added
        List<String> authList = new ArrayList<>();
        authList.add("role01");
        authList.add("role02");
        authList.add("role03");
        authList.add("role04");
        newUser.setAuthority(authList);
        List<GrantedAuthority> authListInserted = userService.addUserAuthorities(newUser);

        //newUser has "role01", "role02", "role03" and "role04"
        assertThat(authListInserted, is(notNullValue()));
        assertThat(authListInserted.size(), is(equalTo(4)));

        //authorities to be deleted
        List<String> deleteList = new ArrayList<>();
        deleteList.add(authList.get(1));    //"role02"
        deleteList.add(authList.get(2));    //"role03"
        newUser.setAuthority(deleteList);

        //delete authorities("role02", "role03")
        this.mvc.perform(delete("/user/"+newUser.getUserNm()+"/authority")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(this.objectMapper.writeValueAsString(newUser)))
            .andDo(log())
            .andExpect(status().isOk());
        
        //expected authority list
        List<GrantedAuthority> tobeList = new ArrayList<>();
        tobeList.add(new SimpleGrantedAuthority("role01"));
        tobeList.add(new SimpleGrantedAuthority("role04"));

        //newUser should has "role01" and "role04"
        List<GrantedAuthority> authListDeleted = userService.getUserAuthorities(newUser);
        assertThat(authListDeleted, is(notNullValue()));
        assertThat(authListDeleted, containsInAnyOrder(tobeList.toArray()));
    }
}