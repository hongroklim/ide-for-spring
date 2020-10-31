package dev.rokong.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.rokong.dto.CategoryDTO;
import dev.rokong.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired CategoryDAO cDAO;

    public List<CategoryDTO> getCategoryList(){
        return cDAO.selectCategoryList();
    };

    public CategoryDTO createCategory(CategoryDTO category){
        if(category.getUpId() != 0){
            //check upId category exists
            this.getCategoryNotNull(category.getUpId());
        }

        //avoid duplicate order with same level
        List<CategoryDTO> siblings = this.getCategoryChildren(category.getUpId());
        if(category.getOrd() == 0){ //max(ord)+1
            category.setOrd(this.maxOrdOfCategory(siblings)+1);
        }else{                      //avoid duplicate ord
            for(CategoryDTO c : siblings){
                if(c.getOrd() == category.getOrd()){
                    //throw exeption and break
                    throw new BusinessException(c.getOrd()+"th ord under "+c.getUpId()+" already exists");
                }
            }
        }

        cDAO.insertCategory(category);

        //TODO : how to know pk?

        return this.getCategoryNotNull(category);
    };

    public void deleteCategory(int id){
        CategoryDTO category = this.getCategoryNotNull(id);
        cDAO.deleteCategory(category.getId());
    };
    
    public CategoryDTO getCategory(int id){
        return cDAO.selectCategory(id);
    };
    
    public CategoryDTO updateCategory(CategoryDTO category){
        CategoryDTO asisCategory = this.getCategoryNotNull(category);

        //if nothing to be changed, return asis DTO
        if(asisCategory.getName().equals(category.getName())){
            if(asisCategory.getUpId() == category.getUpId()){
                log.debug("category's name and upId is equal to asis one");
                return asisCategory;
            }
        }

        if(category.getUpId() != 0){
            //check upId category exists
            this.getCategoryNotNull(category.getUpId());
        }

        List<CategoryDTO> siblings = this.getCategoryChildren(category.getUpId());
        for(CategoryDTO c : siblings){
            if(c.getName().equals(category.getName())){
                //avoid duplicate name
                throw new BusinessException(c.getName()+" name under "+c.getUpId()+" already exists");
            }else if(c.getOrd() == category.getOrd()){
                //avoid duplicate ord
                log.info(c.getOrd()+"th ord under "+c.getUpId()+" already exists");
                
                category.setOrd(this.maxOrdOfCategory(siblings)+1);
                log.info(category.getName()+"'s ord is changed to max(ord)+1");
            }
        }

        if(category.getOrd() == 0){
            //if ord is not initialized, set max(ord)+1 value
            category.setOrd(this.maxOrdOfCategory(siblings)+1);
        }

        cDAO.updateCategory(category);

        return this.getCategoryNotNull(category);
    };
    
    public List<CategoryDTO> getCategoryChildren(int upId){
        return cDAO.selectCategoryChildren(upId);
    };
    
    public CategoryDTO updateCategoryOrder(CategoryDTO category){
        CategoryDTO asisCategory = this.getCategoryNotNull(category);
        if(asisCategory.getOrd() == category.getOrd()){
            //return asisCategory if nothing to be changed
            return asisCategory;
        }

        List<CategoryDTO> siblings = this.getCategoryChildren(category.getUpId());
        for(CategoryDTO c : siblings){
            if(c.getOrd() == category.getOrd()){
                //avoid duplicate ord in same level
                throw new BusinessException(c.getOrd()+" name under "+c.getUpId()+" already exists");
            }
        }

        //execute update
        asisCategory.setOrd(category.getOrd());
        category = asisCategory;
        cDAO.updateCategory(category);

        return this.getCategoryNotNull(category);
    };
    
    private CategoryDTO getCategoryNotNull(int id){
        CategoryDTO category = cDAO.selectCategory(id);
        if(category == null){
            throw new BusinessException(id+"is not exists");
        }
        return category;
    };

    private CategoryDTO getCategoryNotNull(CategoryDTO category){
        if(category.getId() == 0){
            throw new BusinessException("categoryId is not defined");
        }
        return this.getCategoryNotNull(category.getId());
    };

    private int maxOrdOfCategory(List<CategoryDTO> cList){
        int maxOrd = 0;
        
        if(cList == null || cList.size() == 0){
            return maxOrd;
        }

        for(CategoryDTO c : cList){
            if(c.getOrd() > maxOrd){
                maxOrd = c.getOrd();
            }
        }
        return maxOrd;
    }
}
