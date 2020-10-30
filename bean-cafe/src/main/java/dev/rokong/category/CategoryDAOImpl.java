package dev.rokong.category;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.rokong.dto.CategoryDTO;

@Repository
public class CategoryDAOImpl implements CategoryDAO {
    
    public static final String PREFIX = "dev.rokong.CategoryMapper.";

    @Autowired SqlSessionTemplate sqlSession;

    public List<CategoryDTO> selectCategoryList(){
        return sqlSession.selectList(PREFIX+"selectCategoryList");
    };
    
    public void insertCategory(CategoryDTO category){
        sqlSession.insert(PREFIX+"insertCategory", category);
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
}
