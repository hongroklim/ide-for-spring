package com.company.category;

import java.util.List;

import com.company.dto.CategoryDTO;

public interface CategoryDAO {
    public List<CategoryDTO> selectCategoryList();
    public int insertCategory(CategoryDTO category);
    public void deleteCategory(int id);
    public CategoryDTO selectCategory(int id);
    public List<CategoryDTO> selectCategoryChildren(int upId);
    public void updateCategory(CategoryDTO category);
    public void updateCategoryOrder(CategoryDTO category);
    public void backwardChildrenOrder(int upId, int startOrder, int endOrder);
    public void forwardChildrenOrder(int upId, int startOrder, int endOrder);
    public void arrangeChildrenOrder(int upId);
    public boolean isParentAndChild(int parentId, int ChildId);
    public int selectMaxCategoryOrder(int upId);
}
