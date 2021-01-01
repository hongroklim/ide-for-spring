package dev.rokong.category;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.rokong.dto.CategoryDTO;

@RestController
@RequestMapping(value="/category", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(tags={"Category"})
public class CategoryController {

    @Autowired
    private CategoryService cService;

    @RequestMapping(value="", method=RequestMethod.GET)
    @ApiOperation(value="get category list", notes = "get all categories")
    public List<CategoryDTO> getCategoryList(){
        return cService.getCategoryList();
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    @ApiOperation(value="get category", notes = "get category by its id")
    public CategoryDTO getCategory(@PathVariable int id){
        return cService.getCategory(id);
    }

    @RequestMapping(value="/{id}/sub", method=RequestMethod.GET)
    @ApiOperation(value="get category's children",
            notes = "get categories which are its children")
    public List<CategoryDTO> getCategoryChildren(@PathVariable int id){
        return cService.getCategoryChildren(id);
    }

    @RequestMapping(value="", method=RequestMethod.POST)
    @ApiOperation(value="create category",
            notes = "create category. if ord==0, append last [name, upId, ord(0)]")
    public CategoryDTO createCategory(@RequestBody CategoryDTO category){
        return cService.createCategory(category);
    }

    @RequestMapping(value="/{id}/parent", method=RequestMethod.PUT)
    @ApiOperation(value="update category's parent", notes = "change category's parent [id, upId]")
    public CategoryDTO updateCategory(@PathVariable int id, @RequestBody CategoryDTO category){
        if(category != null && category.getId() == 0){
            category.setId(id);
        }
        return cService.updateCategory(category);
    }

    @RequestMapping(value="/{id}/order", method=RequestMethod.PUT)
    @ApiOperation(value="update category's order", notes = "change category's order in siblings [id, ord]")
    public CategoryDTO updateCategoryOrder(@PathVariable int id, @RequestBody CategoryDTO category){
        if(category != null && category.getId() == 0){
            category.setId(id);
        }
        return cService.updateCategoryOrder(category);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    @ApiOperation(value="delete category", notes = "delete category which has no children")
    public void deleteCategory(@PathVariable int id){
        cService.deleteCategory(id);
    }
}