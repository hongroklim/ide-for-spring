package dev.rokong.category;

import java.util.List;

import dev.rokong.dto.CategoryDTO;

public interface CategoryService {
    public List<CategoryDTO> getCategoryList();
    public CategoryDTO getCategory(int id);
    public CategoryDTO getCategoryNotNull(int id);
    public void checkCategoryExist(int id);
    public List<CategoryDTO> getCategoryChildren(int upId);
    public CategoryDTO createCategory(CategoryDTO category);
    public void deleteCategory(int id);
    public CategoryDTO updateCategory(CategoryDTO category);
    public CategoryDTO updateCategoryOrder(CategoryDTO category);
}
