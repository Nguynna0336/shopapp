package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTO {
  @JsonProperty("product_id")
  @Min(value = 1, message = "Product's  ID must > 0 ")
  private Long productId;
  @Size(min = 5, max = 200, message = "image's name??")
  @JsonProperty("image_url")
  private String imageUrl;
}
