package com.example.shopapp.Services;

import com.example.shopapp.Exceptions.DataNotFoundException;
import com.example.shopapp.Exceptions.InvalidParamException;
import com.example.shopapp.Models.Category;
import com.example.shopapp.Models.Product;
import com.example.shopapp.Models.ProductImage;
import com.example.shopapp.Repositories.CategoryRepository;
import com.example.shopapp.Repositories.ProductImageRepository;
import com.example.shopapp.Repositories.ProductRepository;
import com.example.shopapp.Responses.ProductResponse;
import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductImageRepository productImageRepository;
  @Override
  public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
    Category existsCategory =  categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() ->
      new DataNotFoundException("Cann't find category with id: " + productDTO.getCategoryId()));
    Product newProduct = Product.builder()
      .name(productDTO.getName())
      .price(productDTO.getPrice())
      .thumbnail(productDTO.getThumbnail())
      .description(productDTO.getDescription())
      .category(existsCategory)
      .build();
    return productRepository.save(newProduct);
  }

  @Override
  public Product getProductById(Long id) throws Exception {
    return productRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Cann't find product with id: " + id));
  }

  @Override
  public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
    return productRepository.findAll(pageRequest).map(ProductResponse::fromProduct);
  }

  @Override
  public Product updateProduct(Long id, ProductDTO productDTO) throws Exception {
    Product existsingProduct = getProductById(id);
    if(existsingProduct != null) {
      Category existsCategory =  categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() ->
        new DataNotFoundException("Cann't find category with id: " + productDTO.getCategoryId()));
      existsingProduct.setName(productDTO.getName());
      existsingProduct.setCategory(existsCategory);
      existsingProduct.setPrice(productDTO.getPrice());
      existsingProduct.setDescription(productDTO.getDescription());
      existsingProduct.setThumbnail(productDTO.getThumbnail());
      return productRepository.save(existsingProduct);
    }
    return null;
  }

  @Override
  public Void deleteProduct(Long id) {
    Optional<Product> optionalProduct = productRepository.findById(id);
    optionalProduct.ifPresent(productRepository::delete);
    return null;
  }

  @Override
  public Boolean existsByName(String name) {
    return productRepository.existsByName(name);
  }
  @Override
  public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception {
    Product existsingProduct = productRepository.findById(productId).orElseThrow(() ->
        new DataNotFoundException("Cann't find product with id: " + productImageDTO.getProductId()));
    ProductImage newProductImage = ProductImage.builder()
      .product(existsingProduct)
      .imageUrl(productImageDTO.getImageUrl())
      .build();
    int size = productImageRepository.findByProductId(existsingProduct.getId()).size();
    if(size >= ProductImage.maxImages) {
      throw new InvalidParamException("Nubmer of image must be <= " + ProductImage.maxImages);
    }
    return productImageRepository.save(newProductImage);
  }

}
