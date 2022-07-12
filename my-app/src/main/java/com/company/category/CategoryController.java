package com.company.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.company.dto.CategoryDTO;

@RestController
@RequestMapping("/category")
public class CategoryController {
    
    @Autowired CategoryService cService;

    @RequestMapping(value="", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDTO> getCategoryList(){
        return cService.getCategoryList();
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO createCategory(@RequestBody CategoryDTO category){
        return cService.createCategory(category);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO getCategory(@PathVariable int id){
        return cService.getCategory(id);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteCategory(@PathVariable int id){
        cService.deleteCategory(id);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO updateCategory(@RequestBody CategoryDTO category){
        return cService.updateCategory(category);
    }

    @RequestMapping(value="/{id}/sub", method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDTO> getCategoryChildren(@PathVariable int id){
        return cService.getCategoryChildren(id);
    }

    @RequestMapping(value="/{id}/order", method=RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO updateCategoryOrder(@RequestBody CategoryDTO category){
        return cService.updateCategoryOrder(category);
    }
}
