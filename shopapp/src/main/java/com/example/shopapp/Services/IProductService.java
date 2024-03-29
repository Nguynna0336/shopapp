package com.example.shopapp.Services;

import com.example.shopapp.Exceptions.DataNotFoundException;
import com.example.shopapp.Models.Product;
import com.example.shopapp.Models.ProductImage;
import com.example.shopapp.Responses.ProductResponse;
import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


public interface IProductService {
  public Product createProduct(ProductDTO productDTO) throws DataNotFoundException;
  Product getProductById(Long id) throws Exception;
  Page<ProductResponse> getAllProducts(PageRequest pageRequest);
  Product updateProduct(Long id, ProductDTO productDTO) throws Exception;
  Void deleteProduct(Long id);
  Boolean existsByName(String name);
  public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;
}
