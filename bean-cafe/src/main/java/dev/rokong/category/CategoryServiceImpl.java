package dev.rokong.category;

import java.util.List;

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
    
    @Autowired CategoryDAO cDAO;
    @Autowired ProductDAO pDAO;

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

        //insert new category and return id
        int createdId = cDAO.insertCategory(category);
        category.setId(createdId);

        return this.getCategoryNotNull(category);
    };

    public void deleteCategory(int id){
        CategoryDTO category = this.getCategoryNotNull(id);

        List<CategoryDTO> subList = this.getCategoryChildren(category.getId());
        if(subList != null && subList.size() > 0){
            //if there are children to be deleted one,
            //throw exception and return
            throw new BusinessException(category.getId()+"'s children are exists");
        }

        pDAO.updateCategory(id, CategoryDTO.ETC_ID);

        cDAO.deleteCategory(category.getId());
    };
    
    public CategoryDTO getCategory(int id){
        return cDAO.selectCategory(id);
    };
    
    public CategoryDTO updateCategory(CategoryDTO category){
        CategoryDTO asisCategory = this.getCategoryNotNull(category);
        CategoryDTO tobeCategory = new CategoryDTO(asisCategory.getId());

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

            //prevent nested hierarchy
            if(asisCategory.getUpId() == category.getUpId()){
                if(cDAO.isParentAndChild(asisCategory.getId(), category.getUpId())){
                    log.debug("nested hierarchy between "+asisCategory.getId()+" and "+category.getUpId());
                    throw new BusinessException(asisCategory.getId()+" can not be attached under "+category.getUpId());
                }
            }
        }

        //after validating, set up id
        tobeCategory.setUpId(category.getUpId());

        //check order is duplicate
        boolean isOrdDuplicated = false;
        List<CategoryDTO> siblings = this.getCategoryChildren(tobeCategory.getUpId());
        for(CategoryDTO c : siblings){
            if(c.getName().equals(category.getName())){
                //avoid duplicate name
                throw new BusinessException(c.getName()+" name under "+c.getUpId()+" already exists");
            }else if(c.getOrd() == category.getOrd()){
                //avoid duplicate order
                isOrdDuplicated = true;
            }
        }

        //set order
        if(isOrdDuplicated){
            tobeCategory.setOrd(this.maxOrdOfCategory(siblings)+1);
        }else{
            tobeCategory.setOrd(category.getOrd());
        }
        
        //set name
        tobeCategory.setName(category.getName());

        cDAO.updateCategory(tobeCategory);

        return this.getCategoryNotNull(tobeCategory);
    };
    
    public List<CategoryDTO> getCategoryChildren(int upId){
        return cDAO.selectCategoryChildren(upId);
    };
    
    public CategoryDTO updateCategoryOrder(CategoryDTO category){
        CategoryDTO asisCategory = this.getCategoryNotNull(category);
        
        int asisOrder = asisCategory.getOrd();
        int tobeOrder = category.getOrd();

        if(tobeOrder == asisOrder){
            //return asisCategory if nothing to be changed
            return asisCategory;
        }

        //to check tobeOrder beyonds max(order)
        int maxOrd = this.maxOrdOfCategory(category.getUpId());

        //prevent unique constraint (upId, ord)
        this.appendLastCategoryOrder(category);

        if(tobeOrder <= maxOrd){
            //if tobeOrder exists in max(order)

            if(tobeOrder < asisOrder){                      //move forward order
                //move backward between tobe and asis one
                cDAO.backwardChildrenOrder(asisCategory.getUpId(),
                    tobeOrder, asisOrder);
            }else{                                          //move backward order
                //move forward between asis and tobe one
                cDAO.forwardChildrenOrder(asisCategory.getUpId(),
                    asisOrder, tobeOrder);
            }

            //execute update only order
            cDAO.updateCategoryOrder(category);
        }else{
            log.debug("tobe order exceed the max order. it will be appended last");
        }
        
        return this.getCategoryNotNull(category);
    };
    
    public CategoryDTO getCategoryNotNull(int id){
        CategoryDTO category = cDAO.selectCategory(id);
        if(category == null){
            throw new BusinessException(id+" category is not exists");
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

    private int maxOrdOfCategory(int upId){
        return cDAO.selectMaxCategoryOrder(upId);
    }

    private void appendLastCategoryOrder(CategoryDTO category){
        CategoryDTO getCategory = this.getCategoryNotNull(category);
        
        //set order max(ord)+1 value
        getCategory.setOrd(this.maxOrdOfCategory(getCategory.getUpId())+1);

        cDAO.updateCategoryOrder(getCategory);
    }
}
