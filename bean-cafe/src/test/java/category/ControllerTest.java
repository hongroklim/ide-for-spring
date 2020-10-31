package category;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import config.MvcUnitConfig;
import dev.rokong.category.CategoryController;
import dev.rokong.category.CategoryService;
import dev.rokong.dto.CategoryDTO;

public class ControllerTest extends MvcUnitConfig {

    @Autowired CategoryController cController;
    @Autowired CategoryService cService;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(cController).build();
    }
    
    @Test
    public void getCategoryList() throws Exception {
        this.mvc.perform(get("/category"))  //get category list
            .andDo(log())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].upId", is(equalTo(0))))   //first category's upId is 0 (means root)
            .andExpect(jsonPath("$[0].ord", is(equalTo(1))));   //first item's order is also first
    }

    @Test
    public void createCatgory() throws Exception {
        //get category list to extract upId
        List<CategoryDTO> cList = cService.getCategoryList();
        assertThat(cList, is(notNullValue()));
        assertThat(cList.size(), is(greaterThan(1)));

        //create new DTO
        CategoryDTO category = new CategoryDTO();
        category.setName("test01");
        category.setUpId(cList.get(2).getId());

        //create category
        MvcResult result = this.mvc.perform(post("/category")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(this.objectMapper.writeValueAsString(category)))
                            .andReturn();
        String content = result.getResponse().getContentAsString();
        CategoryDTO retCategory = this.objectMapper.readValue(content, CategoryDTO.class);

        //select category after create
        CategoryDTO getCategory = cService.getCategory(retCategory.getId());
        assertThat(getCategory, is(notNullValue()));
        assertThat(getCategory.getName(), is(equalTo(retCategory.getName())));
        assertThat(getCategory.getUpId(), is(equalTo(retCategory.getUpId())));

    }
}
