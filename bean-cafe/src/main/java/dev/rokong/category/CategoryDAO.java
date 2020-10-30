package dev.rokong.category;

import java.util.List;

import dev.rokong.dto.CategoryDTO;

public interface CategoryDAO {
    public List<CategoryDTO> selectCategoryList();
    public void insertCategory(CategoryDTO category);
    public void deleteCategory(int id);
    public CategoryDTO selectCategory(int id);
    public List<CategoryDTO> selectCategoryChildren(int upId);
}
