package dev.rokong.category;

import java.util.List;

import dev.rokong.dto.CategoryDTO;

public interface CategoryDAO {
    public List<CategoryDTO> selectList();
    public CategoryDTO select(int id);
    public List<CategoryDTO> selectChildren(int upId);
    public int selectMaxOrder(int upId);
    public int count(int id);

    public int insert(CategoryDTO category);
    public void delete(int id);
    public void update(CategoryDTO category);
    public void updateOrder(CategoryDTO category);
    public void backwardChildrenOrder(int upId, int startOrder, int endOrder);
    public void forwardChildrenOrder(int upId, int startOrder, int endOrder);
    public void arrangeChildrenOrder(int upId);
    public boolean isParentAndChild(int parentId, int ChildId);
}
