package dev.rokong.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.rokong.category.CategoryService;
import dev.rokong.dto.CategoryDTO;
import dev.rokong.util.RandomUtil;

@Component("MockCategory")
public class MockCategory extends AbstractMockObject<CategoryDTO>{

    @Autowired CategoryService cService;

    @Override
    public CategoryDTO temp() {
        CategoryDTO category = new CategoryDTO();
        category.setName("ctgy-"+RandomUtil.randomString(5));
        category.setUpId(CategoryDTO.ETC_ID);
        return category;
    }

    @Override
    protected CategoryDTO createObjService(CategoryDTO obj) {
        return cService.createCategory(obj);
    }

    @Override
    protected CategoryDTO getObjService(CategoryDTO obj) {
        return cService.getCategory(obj.getId());
    }

}