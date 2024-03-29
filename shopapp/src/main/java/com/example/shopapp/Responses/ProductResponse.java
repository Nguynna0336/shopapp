package com.example.shopapp.Responses;

import com.example.shopapp.Models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse extends BaseResponse {

  private String name;
  private Float price;
  private String thumbnail;
  private String description;
  @JsonProperty("category_id")
  private Long categoryId;
  private List<MultipartFile> files;
  public static ProductResponse fromProduct(Product product) {
    ProductResponse productResponse = ProductResponse.builder()
      .name(product.getName())
      .price(product.getPrice())
      .thumbnail(product.getThumbnail())
      .description(product.getDescription())
      .categoryId(product.getCategory().getId())
      .build();
    productResponse.setCreateAt(product.getCreatedAt());
    productResponse.setUpdateAt(product.getUpdatedAt());
    return productResponse;
  }

}
