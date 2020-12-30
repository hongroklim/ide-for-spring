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
import dev.rokong.category.CategoryDAO;
import dev.rokong.dto.CategoryDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaoTest extends SpringConfig {
    
    @Autowired CategoryDAO cDAO;
    
    @Test
    public void getCategoryList(){
        List<CategoryDTO> cList = cDAO.selectList();
        log.debug(cList.toString());
    }

    @Test
    public void selectKey(){
        //get category list to extract upId
        List<CategoryDTO> cList = cDAO.selectList();
        assertThat(cList, is(notNullValue()));
        assertThat(cList.size(), is(greaterThan(1)));

        //create new DTO
        CategoryDTO category = new CategoryDTO();
        category.setName("test01");
        category.setUpId(cList.get(2).getId());

        //compare returned id with select one
        int selectKey = cDAO.insert(category);
        CategoryDTO getCategory = cDAO.select(selectKey);
        assertThat(getCategory, is(notNullValue()));
        assertThat(getCategory.getId(), is(equalTo(selectKey)));
    }
}
