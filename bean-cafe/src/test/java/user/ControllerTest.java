package user;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import config.MvcUnitConfig;
import dev.rokong.dto.UserDTO;
import dev.rokong.user.UserController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControllerTest extends MvcUnitConfig{
    
    @Autowired UserController userController;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void initConfig(){
        assertThat(this.mvc, is(not(nullValue())));
    }
    
    @Test
    public void simpleRequest() throws Exception{
        this.mvc.perform(get("/user"))
            .andDo(log())
            .andExpect(status().isOk());
    }

    @Test
    public void objectMapper(){
        assertThat(this.objectMapper, is(notNullValue()));
    }

    @Test
    public void createUser() throws Exception{
        UserDTO newUser = new UserDTO();
        newUser.setUserNm("newUser");
        newUser.setPwd("pwd");
        newUser.setEnabled(true);

        log.debug(this.objectMapper.writeValueAsString(newUser));

        this.mvc.perform(post("/user")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(this.objectMapper.writeValueAsString(newUser)))
            .andDo(log())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userNm", is(equalTo(newUser.getUserNm()))))
            .andExpect(jsonPath("$.authority", is(nullValue())));
    }

    /**
     * request user list by MockMvc then
     * parse JSON response as java.Object through Jackson
     * @throws Exception
     */
    @Test
    public void parseJsonResponse() throws Exception {
        //request MockMvc then Return MvcResult by .andReturn()
        MvcResult result = this.mvc.perform(get("/user"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();
        
        //parse response content to string
        String content = result.getResponse().getContentAsString();

        //content returns array in jsonType
        assertThat(content, startsWith("[{"));
        assertThat(content, endsWith("}]"));
        List<UserDTO> userList = this.objectMapper.readValue(content, new TypeReference<List<UserDTO>>(){});

        //userList contains at least one user
        assertThat(userList, is(notNullValue()));
        assertThat(userList.size(), is(greaterThan(0)));
    }

    @Test
    public void updateUser() throws Exception {
        MvcResult result = this.mvc.perform(get("/user")).andReturn();
        String content = result.getResponse().getContentAsString();
        List<UserDTO> userList = this.objectMapper.readValue(content, new TypeReference<List<UserDTO>>(){});

        assertThat(userList, is(notNullValue()));
        assertThat(userList.size(), is(greaterThan(0)));

        UserDTO updateUser = new UserDTO();
        updateUser.setUserNm(userList.get(0).getUserNm());
        updateUser.setPwd("newPassWord");

        this.mvc.perform(patch("/user/"+userList.get(0).getUserNm())
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(this.objectMapper.writeValueAsString(updateUser)))
            .andDo(log())
            .andExpect(status().isMethodNotAllowed());
            //.andExpect(jsonPath("$.userNm", is(equalTo(updateUser.getUserNm()))))
            //.andExpect(jsonPath("$.pwd", is(not(equalTo(userList.get(0).getPwd())))));
    }
}