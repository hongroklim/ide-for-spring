package dev.rokong.category;

import java.util.List;

import dev.rokong.dto.CategoryDTO;

public interface CategoryDAO {
    public List<CategoryDTO> selectCategoryList();
    public int insertCategory(CategoryDTO category);
    public void deleteCategory(int id);
    public CategoryDTO selectCategory(int id);
    public List<CategoryDTO> selectCategoryChildren(int upId);
    public void updateCategory(CategoryDTO category);
    public void updateCategoryOrder(CategoryDTO category);
    public void pushChildrenOrder(int upId, int startOrder);
    public void arrangeChildrenOrder(int upId);
    public boolean isParentAndChild(int parentId, int ChildId);
}
