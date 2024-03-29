package com.example.shopapp.Controllers;

import com.example.shopapp.Exceptions.DataNotFoundException;
import com.example.shopapp.Models.User;
import com.example.shopapp.Services.UserService;
import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.dtos.UserLoginDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
  private final UserService userService;
  @PostMapping("/register")
  public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
    try {
      if (result.hasErrors()) {
        List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
        return ResponseEntity.badRequest().body(errorMessages);
      }
      if(!userDTO.getPassword().equals(userDTO.getReTypePassword())) {
        return ResponseEntity.badRequest().body("Retype password does not match");
      }
      User user = userService.createUser(userDTO);
      return ResponseEntity.ok(user);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<String> login (@Valid @RequestBody UserLoginDTO userLoginDTO)  {
    try {
      String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
      return ResponseEntity.ok(token);
    } catch (DataNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
}
