package category;

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
        List<CategoryDTO> cList = cDAO.selectCategoryList();
        log.debug(cList.toString());
    }
}
