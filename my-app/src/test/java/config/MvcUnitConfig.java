package config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import com.company.main.MainDAO;
import com.company.mock.MockObjects;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
(locations={"file:src/main/webapp/WEB-INF/context/root-context.xml",
    "file:src/main/webapp/WEB-INF/context/app-context.xml"})
@WebAppConfiguration
@Transactional @Rollback
public abstract class MvcUnitConfig {

    protected MockMvc mvc;
    
    @Autowired  @Qualifier("mvcMessageConverter")
    protected MappingJackson2HttpMessageConverter messageConverter;

    protected ObjectMapper objectMapper;
    
    @Autowired @Qualifier("MockObjects")
    protected MockObjects mockObj;

    private @Autowired MainDAO mDAO;

    /**
     * inject Controller into MockMvc. This method always execute
     * ahead test because of <code>@Before</code> annotation
     * <p/>
     * Override Example :
     * <pre> 
     *<code>@Autowired</code> UserController userController;
     *<code>@Override</code>
     *public void setMvc(){
     *    this.mvc = MockMvcBuilders.standaloneSetup(userController).build();
     *}
     *</pre>
     */
    @Before
    public abstract void setMvc();

    @Before
    public void initObjectMapper(){
        this.objectMapper = messageConverter.getObjectMapper();
    }

    /**
     * set auto increment sequences
     * to max values of each table
     * <p>it will perform stored function
     * <code>SELECT reset_serial();</code>
     */
    @After
    public void resetSerial(){
        try{
            mDAO.resetSerial();
        }catch(UncategorizedSQLException e){
            //exception because a test failed
        }
        
    }

    /**
     * 
     * @param url request URL
     * @param method request method : GET, POST, PUT, PATCH, DELETE
     * @return MockHttpServletRequestBuilder
     * @throws IllegalArgumentException HEAD method passed
     */
    protected MockHttpServletRequestBuilder initMockRequest(String url, RequestMethod method){
        MockHttpServletRequestBuilder mockReqMethod = null;
        
        switch (method) {
            case GET:
                mockReqMethod = get(url);
                break;
            
            case POST:
                mockReqMethod = post(url);
                break;
            
            case PUT:
                mockReqMethod = put(url);
                break;
            
            case PATCH:
                mockReqMethod = patch(url);
                break;

            case DELETE:
                mockReqMethod = delete(url);
                break;
            
            default:
                
        }

        if(mockReqMethod != null){
            return mockReqMethod;
        }

        throw new IllegalArgumentException(method.name()+" method is not defined");
    }

    /**
     * perform MVC request and get responseBody
     * response status is expected to 200(OK)
     * 
     * @param mockReqMethod MockHttpServletRequestBuilder
     * @param reqObject request Object to be written in RequestBody
     * @return response content as string
     * @throws Exception from MockMvc.perform(...)
     */
    protected String requestAndGetResponseBody(MockHttpServletRequestBuilder mockReqMethod,
        Object reqObject) throws Exception {

        MvcResult result = this.mvc.perform(mockReqMethod
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(this.objectMapper.writeValueAsString(reqObject)))
                            .andDo(log())
                            .andExpect(status().isOk())
                            .andReturn();
        
        return result.getResponse().getContentAsString();
    }

    /**
     * MockHttpRequestBody and ResponseBody template for Spring REST Test.
     * <p/>
     * For example:
     * <pre>reqAndResBody("/user/"+user.getId(), RequestMethod.GET, user, UserDTO.class)</pre>
     * 
     * @param <T> ResponseBody type
     * @param url request URL
     * @param method request method : GET, POST, PUT, PATCH, DELETE
     * @param reqObject requestBody Object
     * @param resClass ResponseBody.Class or <code>null</code>
     * @return ResponseBody Object
     * @throws IllegalArgumentException HEAD passed into RequestMethod method parameter
     * @throws Exception from MockMvc.perform(...)
     */
    protected <T> T reqAndResBody(String url, RequestMethod method,
        Object reqObject, Class<T> resClass) throws Exception {
        
        MockHttpServletRequestBuilder mockReqMethod = this.initMockRequest(url, method);
        String content = requestAndGetResponseBody(mockReqMethod, reqObject);
        if(resClass != null){
            return this.objectMapper.readValue(content, resClass);
        }else{
            return null;
        }
    }

    /**
     *  MockHttpRequestBody and ResponseBody template for Spring REST Test.
     *  <p/>
     *  This method return List of {@link #reqAndResBody(String, RequestMethod, Object, resClass)}
     * 
     * @param <T> ResponseBody type
     * @param url request URL
     * @param method request method : GET, POST, PUT, PATCH, DELETE
     * @param reqObject requestBody Object
     * @return ResponseBody Object
     * @throws IllegalArgumentException HEAD passed into RequestMethod method parameter
     * @throws Exception from MockMvc.perform(...)
     * @deprecated this function returns LinkedHashMap {@link #reqAndResBodyList(String, RequestMethod, Object, Class))}
     */
    protected <T> List<T> reqAndResBody(String url, RequestMethod method,
        Object reqObject) throws Exception {
        
        MockHttpServletRequestBuilder mockReqMethod = this.initMockRequest(url, method);
        String content = requestAndGetResponseBody(mockReqMethod, reqObject);
        return this.objectMapper.readValue(content, new TypeReference<List<T>>() {});
    }

    /**
     * MockHttpRequestBody and ResponseBody template for Spring REST Test.
     * <p/>
     * This method return List of {@link #reqAndResBody(String, RequestMethod, Object, resClass)}
     * 
     * @param <T> ResponseBody type
     * @param url request URL
     * @param method request method : GET, POST, PUT, PATCH, DELETE
     * @param reqObject requestBody Object
     * @param resClass ResponseBody.Class
     * @return ResponseBody Objects in List type
     * @throws IllegalArgumentException HEAD passed into RequestMethod method parameter
     * @throws Exception from MockMvc.perform(...)
     */
    protected <T> List<T> reqAndResBodyList(String url, RequestMethod method,
        Object reqObject, Class<T> resClass) throws Exception {
        
        MockHttpServletRequestBuilder mockReqMethod = this.initMockRequest(url, method);
        String content = requestAndGetResponseBody(mockReqMethod, reqObject);
        CollectionType listType = this.objectMapper.getTypeFactory().constructCollectionType(List.class, resClass);
        return this.objectMapper.readValue(content, listType);
    }
}