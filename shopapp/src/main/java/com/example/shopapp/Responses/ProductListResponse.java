package com.example.shopapp.Responses;

import com.example.shopapp.Models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class ProductListResponse {
  private List<ProductResponse> productResponses;
  private int totalPages;

}
