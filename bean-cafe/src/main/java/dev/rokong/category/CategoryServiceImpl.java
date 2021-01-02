package dev.rokong.category;

import java.util.List;

import dev.rokong.util.ObjUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.rokong.dto.CategoryDTO;
import dev.rokong.exception.BusinessException;
import dev.rokong.product.main.ProductDAO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private ProductDAO productDAO;

    public List<CategoryDTO> getCategoryList(){
        return categoryDAO.selectList();
    };

    public CategoryDTO createCategory(CategoryDTO category){
        if(category.getUpId() != 0){
            //check upId category exists
            this.checkCategoryExist(category.getUpId());
        }

        //avoid duplicate order with same level
        List<CategoryDTO> siblings = this.getCategoryChildren(category.getUpId());
        if(category.getOrd() == null || category.getOrd() == 0){ //max(ord)+1
            category.setOrd(this.maxOrdOfCategory(siblings)+1);
        }else{                      //avoid duplicate ord
            for(CategoryDTO c : siblings){
                if(c.getOrd().equals(category.getOrd())){
                    //throw exception and break
                    throw new BusinessException(c.getOrd()+"th ord under "+c.getUpId()+" already exists");
                }
            }
        }

        //insert new category and return id
        int createdId = categoryDAO.insert(category);
        category.setId(createdId);

        return this.getCategoryNotNull(category);
    }

    public void deleteCategory(int id){
        this.checkCategoryExist(id);

        List<CategoryDTO> subList = this.getCategoryChildren(id);
        if(subList != null && subList.size() > 0){
            //if there are children to be deleted one,
            //throw exception and return
            throw new BusinessException(id+"'s children are exists");
        }

        productDAO.updateCategory(id, CategoryDTO.ETC_ID);

        categoryDAO.delete(id);
    }
    
    public CategoryDTO getCategory(int id){
        return categoryDAO.select(id);
    };
    
    public CategoryDTO updateCategory(CategoryDTO category){
        if(category == null){
            throw new IllegalArgumentException("category is not defined");
        }

        CategoryDTO asisCategory = this.getCategoryNotNull(category);
        CategoryDTO tobeCategory = new CategoryDTO(asisCategory.getId());

        boolean changeName = ObjUtil.isNotEmpty(category.getName()) && !asisCategory.getName().equals(category.getName());
        boolean changeUpId = category.getUpId()!=null && !asisCategory.getUpId().equals(category.getUpId());
        boolean changeOrd = category.getOrd()!=null && !asisCategory.getOrd().equals(category.getOrd());

        //if nothing to be changed, return asis DTO
        if(!changeName && !changeUpId & !changeOrd){
            log.debug("category's ord, name, upId are equal to asis one");
            return asisCategory;
        }

        //if upId is going to be changed
        if(changeUpId){
            //check upId category exists
            this.checkCategoryExist(category.getUpId());

            //prevent nested hierarchy
            if(categoryDAO.isParentAndChild(asisCategory.getId(), category.getUpId())){
                log.debug("nested hierarchy between "+category.getId()+" and "+category.getUpId());
                throw new BusinessException(category.getId()+" can not be attached under "+category.getUpId());
            }

            //after validating, set up id
            tobeCategory.setUpId(category.getUpId());
        }else{
            //or set asis one
            tobeCategory.setUpId(asisCategory.getUpId());
        }

        if(changeName || changeUpId){
            //avoid duplicate name
            List<CategoryDTO> siblings = this.getCategoryChildren(tobeCategory.getUpId());
            for(CategoryDTO c : siblings){
                if(c.getId() != category.getId() && c.getName().equals(category.getName())){
                    //same name with different id
                    throw new BusinessException(c.getName()+" name under "+c.getUpId()+" already exists");
                }
            }

            //set name
            tobeCategory.setName(category.getName());
        }

        if(changeName || changeUpId){
            //update name or upId
            categoryDAO.update(tobeCategory);
        }

        //if ord is defined, update order
        if(category.getOrd() != null){
            this.updateCategoryOrder(category);
        }

        return this.getCategoryNotNull(tobeCategory);
    }

    public void checkCategoryExist(int id){
        if(id == 0){
            throw new IllegalArgumentException("category id is not defined");
        }

        if (categoryDAO.count(id) == 0) {
            throw new BusinessException(id+" category is not exists");
        }
    }

    public List<CategoryDTO> getCategoryChildren(int upId){
        return categoryDAO.selectChildren(upId);
    }
    
    private void updateCategoryOrder(CategoryDTO category){
        CategoryDTO asisCategory = this.getCategoryNotNull(category);
        
        int asisOrder = asisCategory.getOrd();
        int tobeOrder = category.getOrd();

        if(tobeOrder == asisOrder){
            //return asisCategory if nothing to be changed
            return;
        }

        //to check tobeOrder beyonds max(order)
        int maxOrd = this.maxOrdOfCategory(asisCategory.getUpId());

        //prevent unique constraint (upId, ord)
        this.appendLastCategoryOrder(category);

        if(tobeOrder <= maxOrd){
            //if tobeOrder exists in max(order)

            if(tobeOrder < asisOrder){                      //move forward order
                //move backward between tobe and asis one
                categoryDAO.backwardChildrenOrder(asisCategory.getUpId(),
                    tobeOrder, asisOrder);
            }else{                                          //move backward order
                //move forward between asis and tobe one
                categoryDAO.forwardChildrenOrder(asisCategory.getUpId(),
                    asisOrder, tobeOrder);
            }

            //execute update only order
            categoryDAO.updateOrder(category);
        }else{
            log.debug("tobe order exceed the max order. it will be appended last");
        }
    }
    
    public CategoryDTO getCategoryNotNull(int id){
        CategoryDTO category = categoryDAO.select(id);
        if(category == null){
            throw new BusinessException(id+" category is not exists");
        }
        return category;
    }

    private CategoryDTO getCategoryNotNull(CategoryDTO category){
        if(category == null){
            throw new IllegalArgumentException("category is null");

        }else if(category.getId() == 0){
            throw new BusinessException("category id is not defined");
        }
        return this.getCategoryNotNull(category.getId());
    }

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

    private int maxOrdOfCategory(int upId){
        return categoryDAO.selectMaxOrder(upId);
    }

    private void appendLastCategoryOrder(CategoryDTO category){
        CategoryDTO getCategory = this.getCategoryNotNull(category);
        
        //set order max(ord)+1 value
        getCategory.setOrd(this.maxOrdOfCategory(getCategory.getUpId())+1);

        categoryDAO.updateOrder(getCategory);
    }
}
