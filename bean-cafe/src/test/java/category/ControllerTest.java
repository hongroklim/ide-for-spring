package category;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;

import config.MvcUnitConfig;
import dev.rokong.category.CategoryController;
import dev.rokong.category.CategoryService;
import dev.rokong.dto.CategoryDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControllerTest extends MvcUnitConfig {

    @Autowired
    CategoryController cController;
    @Autowired
    CategoryService cService;

    @Override
    public void setMvc() {
        this.mvc = MockMvcBuilders.standaloneSetup(cController).build();
    }

    @Test
    public void getCategoryList() throws Exception {
        this.mvc.perform(get("/category")) // get category list
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].upId", is(equalTo(0)))) // first category's upId is 0 (means root)
                .andExpect(jsonPath("$[0].ord", is(equalTo(1)))); // first item's order is also first
    }

    @Test
    public void createCatgory() throws Exception {
        // get category list to extract upId
        List<CategoryDTO> cList = cService.getCategoryList();
        assertThat(cList, is(notNullValue()));
        assertThat(cList.size(), is(greaterThan(1)));

        // create new DTO
        CategoryDTO category = new CategoryDTO();
        category.setName("test01");
        category.setUpId(cList.get(2).getId());

        // create category
        MvcResult result = this.mvc.perform(post("/category").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(category))).andReturn();
        String content = result.getResponse().getContentAsString();
        CategoryDTO retCategory = this.objectMapper.readValue(content, CategoryDTO.class);

        // select category after create
        CategoryDTO getCategory = cService.getCategory(retCategory.getId());
        assertThat(getCategory, is(notNullValue()));
        assertThat(getCategory.getName(), is(equalTo(retCategory.getName())));
        assertThat(getCategory.getUpId(), is(equalTo(retCategory.getUpId())));
    }

    @Test
    public void getCategory() throws Exception {
        // get category list
        List<CategoryDTO> cList = cService.getCategoryList();
        assertThat(cList, is(notNullValue()));
        assertThat(cList.size(), is(greaterThan(0)));

        // then get one
        CategoryDTO category = cList.get(0);

        // get category through mvc
        MvcResult result = this.mvc.perform(get("/category/" + category.getId())).andDo(log()).andReturn();
        String content = result.getResponse().getContentAsString();
        CategoryDTO getCategory = this.objectMapper.readValue(content, CategoryDTO.class);

        assertThat(getCategory, is(notNullValue()));
        assertThat(getCategory.getId(), is(equalTo(category.getId())));
    }

    @Test
    public void deleteCategory() throws Exception {
        // get category list
        List<CategoryDTO> cList = cService.getCategoryList();
        assertThat(cList, is(notNullValue()));
        assertThat(cList.size(), is(greaterThan(0)));

        // then get one which has no children
        CategoryDTO category = null;
        List<CategoryDTO> subList = null;

        for (CategoryDTO c : cList) {
            subList = cService.getCategoryChildren(c.getId());
            if (subList == null || subList.size() == 0) {
                // if category has no children, then return itself
                category = c;
                break;
            }
        }

        if (category == null) {
            log.debug("can not find category which has no children");
            throw new Exception("no available category to be deleted");
        }

        // delete category
        this.mvc.perform(delete("/category/" + category.getId())).andDo(log()).andExpect(status().isOk());
    }

    @Test
    public void updateCategoryName() throws Exception {
        // get category list to get upId
        List<CategoryDTO> cList = cService.getCategoryList();
        assertThat(cList, is(notNullValue()));
        assertThat(cList.size(), is(greaterThan(1)));

        // create new Category
        CategoryDTO category = new CategoryDTO();
        category.setName("test01");
        category.setUpId(cList.get(2).getId());
        category = cService.createCategory(category);
        int cId = category.getId();

        // update Category's name
        category = new CategoryDTO();
        category.setId(cId);
        category.setName("test02");

        MvcResult result = this.mvc.perform(put("/category/" + category.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(this.objectMapper.writeValueAsString(category)))
                .andDo(log()).andReturn();
        String content = result.getResponse().getContentAsString();
        CategoryDTO getCategory = this.objectMapper.readValue(content, CategoryDTO.class);

        assertThat(getCategory, is(notNullValue()));
        assertThat(getCategory.getName(), is(equalTo(category.getName())));
    }

    @Test
    public void updateCategoryNameThroughFunction() throws Exception {
        // get category list to get upId
        List<CategoryDTO> cList = cService.getCategoryList();
        assertThat(cList, is(notNullValue()));
        assertThat(cList.size(), is(greaterThan(1)));

        // create new Category
        CategoryDTO category = new CategoryDTO();
        category.setName("test01");
        category.setUpId(cList.get(2).getId());
        category = cService.createCategory(category);
        int cId = category.getId();

        // update Category's name
        category = new CategoryDTO();
        category.setId(cId);
        category.setName("test02");

        CategoryDTO getCategory = this.reqAndResBody("/category/"+category.getId(),
            RequestMethod.PUT, category, CategoryDTO.class);

        assertThat(getCategory, is(notNullValue()));
        assertThat(getCategory.getName(), is(equalTo(category.getName())));
    }

    @Test
    public void updateCategoryUpId() throws Exception {
        // get category list to get upId
        List<CategoryDTO> cList = cService.getCategoryList();
        assertThat(cList, is(notNullValue()));
        assertThat(cList.size(), is(greaterThan(1)));

        // create new Category
        CategoryDTO newCategory = new CategoryDTO();
        newCategory.setName("test01");
        newCategory.setUpId(cList.get(2).getId());
        newCategory = cService.createCategory(newCategory);

        // update Category's upId
        CategoryDTO category = newCategory;
        category.setUpId(cList.get(1).getId());

        CategoryDTO getCategory = this.reqAndResBody("/category/"+category.getId(),
            RequestMethod.PUT, category, CategoryDTO.class);
        
        assertThat(getCategory, is(notNullValue()));
        assertThat(getCategory.getUpId(), is(equalTo(category.getUpId())));
    }

    @Test
    public void getCategoryChildren() throws Exception {
        List<CategoryDTO> cList = cService.getCategoryList();
        assertThat(cList, is(notNullValue()));

        //get id which has children
        int CategoryId = 0;
        for(CategoryDTO c : cList){
            if(c.getUpId() != 0){
                CategoryId = c.getUpId();
                break;
            }
        }
        assertThat(CategoryId, is(not(equalTo(0))));

        CategoryDTO category = cService.getCategory(CategoryId);
        List<CategoryDTO> getList = this.reqAndResBodyList("/category/"+category.getId()+"/sub",
            RequestMethod.GET, null, CategoryDTO.class);

        assertThat(getList, is(notNullValue()));
        assertThat(getList.size(), is(greaterThan(0)));
        assertThat(getList.get(0).getUpId(), is(equalTo(CategoryId)));
    }

    @Test
    public void updateCategoryOrder() throws Exception {
        //get category list
        List<CategoryDTO> cList = cService.getCategoryList();
        assertThat(cList, is(notNullValue()));

        //get id which has children
        int upId = 0;
        List<CategoryDTO> subList = null;
        for(CategoryDTO c : cList){
            if(c.getUpId() != 0){
                //get subList 
                subList = cService.getCategoryChildren(c.getUpId());
                if(subList != null && subList.size() >= 2){
                    //find upId which has two more children
                    upId = c.getUpId();
                    break;
                }
            }
        }
        assertThat(upId, is(not(equalTo(0))));
        assertThat(subList.size(), is(greaterThan(1)));

        //get category and order to change
        CategoryDTO category0 = subList.get(0);
        CategoryDTO category1 = subList.get(1);
        
        //set new order into category
        category0.setOrd(category1.getOrd());

        CategoryDTO getCategory = this.reqAndResBody("/category/"+category0.getId()+"/order",
            RequestMethod.PUT, category0, CategoryDTO.class);

        //same id, change order
        assertThat(getCategory, is(notNullValue()));
        assertThat(getCategory.getId(), is(equalTo(category0.getId())));
        assertThat(getCategory.getOrd(), is(equalTo(category0.getOrd())));

        //privious owner of order also changed consequently 
        CategoryDTO getCategory1 = cService.getCategory(category1.getId());
        assertThat(getCategory1, is(notNullValue()));
        assertThat(getCategory1.getOrd(), is(not(equalTo(category1.getOrd()))));
    }

}
