package com.example.shopapp.Controllers;

import com.example.shopapp.Models.Product;
import com.example.shopapp.Models.ProductImage;
import com.example.shopapp.Responses.ProductResponse;
import com.example.shopapp.Services.ProductService;
import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductsController {
  private final ProductService productService;
  @GetMapping("")
  public ResponseEntity<List<ProductResponse>> getProducts(@RequestParam("page") int page, @RequestParam("limit") int limit) {
    PageRequest pageRequest = PageRequest.of(page,limit, Sort.by("createdAt").descending());
    Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
    int totalPage = productPage.getTotalPages();
    List<ProductResponse> products =  productPage.getContent();
    return ResponseEntity.ok(products);
  }
  @GetMapping("/{id}")
  public  ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
    try {
      Product existingProduct = productService.getProductById(productId);
      return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
  @PostMapping(value = "")
  public  ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO,
                                          BindingResult result) {
    try {
      if (result.hasErrors()) {
        List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
        return ResponseEntity.badRequest().body(errorMessages);
      }
      Product newProduct = productService.createProduct(productDTO);
    return ResponseEntity.ok(newProduct);
  }catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
  @PostMapping(value = "/uploadimage/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> uploadImages(@PathVariable("id") Long productId, @ModelAttribute("files") List<MultipartFile> files) {

    try {
      Product existsProduct = productService.getProductById(productId);
      files = files == null ? new ArrayList<MultipartFile>() : files;
      if(files.size() > ProductImage.maxImages) {
        return  ResponseEntity.badRequest().body("You can only upload maxium 5 images");
      }
      List<ProductImage> productImages = new ArrayList<>();
      for (MultipartFile file : files) {
        if (file.getSize() ==0) {
          continue;
        }
        if (file.getSize() > 10 * 1024 * 1024)//10mb//
        {
          return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large! Maximum size is 10MB");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
          return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
        }
        String fileName = storeFile(file);
        ProductImage productImage = productService.createProductImage(existsProduct.getId(), ProductImageDTO.builder().imageUrl(fileName).build());
        productImages.add(productImage);
      }
      return ResponseEntity.ok().body(productImages);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
  public String storeFile(MultipartFile file) throws IOException {
    if (!isImageFile(file) || file.getOriginalFilename() == null) {
        throw new IOException("Invalid image format");
    }
    String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
    Path uploadDir = Paths.get("uploads");
    if (!Files.exists(uploadDir)) {
      Files.createDirectories(uploadDir);
    }
    Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
    Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
    return uniqueFileName;
  }
  private boolean isImageFile(MultipartFile file) {
    String contentType = file.getContentType();
    return contentType != null && contentType.startsWith("image/");
  }
  @PutMapping("/{id}")
  public  ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO ) {
    try {
      Product updatedProduct = productService.updateProduct(id, productDTO);
      return ResponseEntity.ok(updatedProduct);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
  @DeleteMapping("/{id}")
  public  ResponseEntity<String> deleteProduct(@PathVariable long id) {
    try {
      productService.deleteProduct(id);
      return ResponseEntity.ok(String.format("Product with id = %d deleted successfully", id));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
  @PostMapping("/generateFakeProducts")
  public ResponseEntity<String> generateFakeProducts() {
    Faker faker = new Faker();
    for (int i =0; i<1000;i++) {
      String productName = faker.commerce().productName();
      if(productService.existsByName(productName)){
        continue;
      }
      ProductDTO productDTO = ProductDTO.builder()
        .name(productName)
        .price((float)faker.number().numberBetween(10,100000000))
        .description(faker.lorem().sentence())
        .thumbnail("")
        .categoryId((long)faker.number().numberBetween(1,5))
        .build();
      try {
        productService.createProduct(productDTO);
      } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
      }
    }
    return ResponseEntity.ok("Fake products created successfully");
  }
}
