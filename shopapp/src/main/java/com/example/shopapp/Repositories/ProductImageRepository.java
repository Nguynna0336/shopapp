package com.example.shopapp.Repositories;

import com.example.shopapp.Models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
  List<ProductImage> findByProductId(Long productId);
}
