package category;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import config.SpringConfig;
import com.company.category.CategoryDAO;
import com.company.dto.CategoryDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoTest extends SpringConfig {
    
    @Autowired CategoryDAO cDAO;
    
    @Test
    public void getCategoryList(){
        List<CategoryDTO> cList = cDAO.selectCategoryList();
        log.debug(cList.toString());
    }

    @Test
    public void selectKey(){
        //get category list to extract upId
        List<CategoryDTO> cList = cDAO.selectCategoryList();
        assertThat(cList, is(notNullValue()));
        assertThat(cList.size(), is(greaterThan(1)));

        //create new DTO
        CategoryDTO category = new CategoryDTO();
        category.setName("test01");
        category.setUpId(cList.get(2).getId());

        //compare returned id with select one
        int selectKey = cDAO.insertCategory(category);
        CategoryDTO getCategory = cDAO.selectCategory(selectKey);
        assertThat(getCategory, is(notNullValue()));
        assertThat(getCategory.getId(), is(equalTo(selectKey)));
    }
}
