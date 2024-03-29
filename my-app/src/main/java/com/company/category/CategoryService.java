package com.company.category;

import java.util.List;

import com.company.dto.CategoryDTO;

public interface CategoryService {
    public List<CategoryDTO> getCategoryList();
    public CategoryDTO createCategory(CategoryDTO category);
    public void deleteCategory(int id);
    public CategoryDTO getCategory(int id);
    public CategoryDTO getCategoryNotNull(int id);
    public CategoryDTO updateCategory(CategoryDTO category);
    public List<CategoryDTO> getCategoryChildren(int upId);
    public CategoryDTO updateCategoryOrder(CategoryDTO category);
}
