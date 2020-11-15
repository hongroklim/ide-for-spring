package dev.rokong.mock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.rokong.category.CategoryService;
import dev.rokong.dto.CategoryDTO;

@Component("MockCategory")
public class MockCategory extends MockObjects {
    
    private List<CategoryDTO> categoryList = new ArrayList<CategoryDTO>();

    @Autowired CategoryService cService;

    public CategoryDTO tempCategory(){
        CategoryDTO category = new CategoryDTO();
        category.setName("ctgy-"+this.randomString(5));
        return category;
    }

    private CategoryDTO createCategory(){
        CategoryDTO category = this.tempCategory();
        category.setUpId(CategoryDTO.ETC_ID);
        return cService.createCategory(category);
    }

    private boolean isValidList(){
        if(this.categoryList.size() == 0){
            return true;
        }else{
            return cService.getCategory(this.categoryList.get(0).getId()) != null;
        }
    }

    public CategoryDTO anyCategory() {
        if(!this.isValidList()){
            this.categoryList.clear();
        }

        if(this.categoryList.size() == 0){
            this.categoryList.add(this.createCategory());
        }

        return this.categoryList.get(0);
    }

    public List<CategoryDTO> anyCategoryList(int count){
        if(!this.isValidList()){
            this.categoryList.clear();
        }
        
        while(this.categoryList.size() < count){
            this.categoryList.add(this.createCategory());
        }
        return this.categoryList.subList(0, count);
    }
}