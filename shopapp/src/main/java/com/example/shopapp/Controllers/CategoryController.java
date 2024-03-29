package com.example.shopapp.Controllers;

import com.example.shopapp.Models.Category;
import com.example.shopapp.Repositories.CategoryRepository;
import com.example.shopapp.Services.CategoryService;
import com.example.shopapp.dtos.CategoryDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
//@Valid
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;
  @PostMapping("")
  public  ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
    if (result.hasErrors()) {
      List<String> errorMessages =  result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
      return ResponseEntity.badRequest().body(errorMessages);
    }
    categoryService.createCategory(categoryDTO);
    return ResponseEntity.ok("Create successfully");
  }
  @GetMapping("")
  public ResponseEntity<List<Category>> getAllCategories(@RequestParam("page") int page, @RequestParam("limit") int limit) {
    List<Category> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(categories);
  }
  @PutMapping("/{id}")
  public  ResponseEntity<String> updateCategory(@PathVariable long id, @RequestBody CategoryDTO categoryDTO) {
    categoryService.updateCategory(id,categoryDTO);
    return ResponseEntity.ok("Update category successfully" + id);
  }
  @DeleteMapping("/{id}")
  public  ResponseEntity<String> deleteCategory(@PathVariable long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.ok("Delete category successfully");
  }
}
