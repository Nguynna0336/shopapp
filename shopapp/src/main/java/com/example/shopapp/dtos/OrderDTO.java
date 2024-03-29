package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
  @JsonProperty("user_id")
  @Min(value = 1, message = "User's ID must be greater than 0")
  private Long userID;
  private String fullName;
  private String email;
  @JsonProperty("phone_number")
  @NotBlank(message = "Phone number can't be empty")
  @Size(min = 5,message = "phone number must be at least 5 characters" )
  private String phoneNumber;
  private String address;
  private String note;
  @JsonProperty("total_money")
  @Min(value = 0, message = "Total must be greater than or equal to 0")
  private Float totalMoney;
  @JsonProperty("shipping_method")
  private String shippingMethod;
  @JsonProperty("shipping_adress")
  private String shippingAddress;
  @JsonProperty("payment_method")
  private String paymentMethod;
  @JsonProperty("shipping_date")
  private LocalDate shippingDate;

}
