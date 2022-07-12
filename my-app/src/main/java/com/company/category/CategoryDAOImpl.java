package com.company.category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.company.dto.CategoryDTO;

@Repository
public class CategoryDAOImpl implements CategoryDAO {
    
    public static final String PREFIX = "com.company.category.";

    @Autowired SqlSessionTemplate sqlSession;

    public List<CategoryDTO> selectCategoryList(){
        return sqlSession.selectList(PREFIX+"selectCategoryList");
    };
    
    public int insertCategory(CategoryDTO category){
        sqlSession.insert(PREFIX+"insertCategory", category);
        return category.getId();
    };

    public void deleteCategory(int id){
        sqlSession.delete(PREFIX+"deleteCategory", id);
    };

    public CategoryDTO selectCategory(int id){
        return sqlSession.selectOne(PREFIX+"selectCategory", id);
    };

    public List<CategoryDTO> selectCategoryChildren(int upId){
        return sqlSession.selectList(PREFIX+"selectCategoryChildren", upId);
    };

    public void updateCategory(CategoryDTO category){
        sqlSession.update(PREFIX+"updateCategory", category);
    }

    public void updateCategoryOrder(CategoryDTO category){
        sqlSession.update(PREFIX+"updateCategoryOrder", category);
    }

    public void backwardChildrenOrder(int upId, int startOrder, int endOrder){
        Map<String, Integer> hm = new HashMap<String, Integer>();
        hm.put("upId", upId);
        hm.put("startOrder", startOrder);
        hm.put("endOrder", endOrder);
        sqlSession.update(PREFIX+"backwardChildrenOrder", hm);
    }

    public void forwardChildrenOrder(int upId, int startOrder, int endOrder){
        Map<String, Integer> hm = new HashMap<String, Integer>();
        hm.put("upId", upId);
        hm.put("startOrder", startOrder);
        hm.put("endOrder", endOrder);
        sqlSession.update(PREFIX+"forwardChildrenOrder", hm);
    }

    public void arrangeChildrenOrder(int upId){
        sqlSession.update(PREFIX+"arrangeChildrenOrder", upId);
    }

    public boolean isParentAndChild(int parentId, int ChildId){
        Map<String, Integer> hm = new HashMap<String, Integer>();
        hm.put("parentId", parentId);
        hm.put("ChildId", ChildId);
        return sqlSession.selectOne(PREFIX+"isParentAndChild", hm);
    }

    public int selectMaxCategoryOrder(int upId){
        return sqlSession.selectOne(PREFIX+"selectMaxCategoryOrder", upId);
    }
}
