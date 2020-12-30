package dev.rokong.category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dev.rokong.dto.CategoryDTO;

@Repository
public class CategoryDAOImpl implements CategoryDAO {
    
    public static final String PREFIX = "dev.rokong.category.";

    @Autowired SqlSessionTemplate sqlSession;

    public List<CategoryDTO> selectList(){
        return sqlSession.selectList(PREFIX+"selectList");
    };
    
    public int insert(CategoryDTO category){
        sqlSession.insert(PREFIX+"insert", category);
        return category.getId();
    };

    public void delete(int id){
        sqlSession.delete(PREFIX+"delete", id);
    };

    public CategoryDTO select(int id){
        return sqlSession.selectOne(PREFIX+"select", id);
    };

    public List<CategoryDTO> selectChildren(int upId){
        return sqlSession.selectList(PREFIX+"selectChildren", upId);
    };

    public int count(int id){
        return sqlSession.selectOne(PREFIX+"count", id);
    }

    public void update(CategoryDTO category){
        sqlSession.update(PREFIX+"update", category);
    }

    public void updateOrder(CategoryDTO category){
        sqlSession.update(PREFIX+"updateOrder", category);
    }

    public void backwardChildrenOrder(int upId, int startOrder, int endOrder){
        Map<String, Integer> hm = new HashMap<>();
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
        Map<String, Integer> hm = new HashMap<>();
        hm.put("parentId", parentId);
        hm.put("ChildId", ChildId);
        return sqlSession.selectOne(PREFIX+"isParentAndChild", hm);
    }

    public int selectMaxOrder(int upId){
        return sqlSession.selectOne(PREFIX+"selectMaxOrder", upId);
    }
}
