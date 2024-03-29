package com.example.shopapp.Services;

import com.example.shopapp.Models.Category;
import com.example.shopapp.dtos.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
  Category createCategory(CategoryDTO category);
  Category getCategoryById(Long id);
  List<Category> getAllCategories();
  Category updateCategory(Long id, CategoryDTO category);
  void deleteCategory(Long id);
}
