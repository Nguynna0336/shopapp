package com.example.shopapp.Responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
  @JsonProperty("createdAt")
  private LocalDateTime createAt;

  @JsonProperty("updatedAt")
  private LocalDateTime updateAt;

}
