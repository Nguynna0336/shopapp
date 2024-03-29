package com.example.shopapp.Services;

import com.example.shopapp.Models.Category;
import com.example.shopapp.Repositories.CategoryRepository;
import com.example.shopapp.dtos.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService implements ICategoryService{
  private final CategoryRepository categoryRepository;
  @Override
  public Category createCategory(CategoryDTO category) {
    Category newcategory = Category.builder().name(category.getName()).build();
    return categoryRepository.save(newcategory);
  }

  @Override
  public Category getCategoryById(Long id) {
    return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
  }

  @Override
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @Override
  public Category updateCategory(Long id, CategoryDTO category) {
    Category existingCategory = getCategoryById(id);
    existingCategory.setName(category.getName());
    categoryRepository.save(existingCategory);
    return existingCategory;
  }

  @Override
  public void deleteCategory(Long id) {
     categoryRepository.deleteById(id);
  }
}
