package com.example.shopapp.Controllers;

import com.example.shopapp.Models.Order;
import com.example.shopapp.Services.IOrderService;
import com.example.shopapp.dtos.OrderDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
  private final IOrderService orderService;
  @PostMapping("")
  public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult result) {
    try {
      if (result.hasErrors()) {
        List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
        return ResponseEntity.badRequest().body(errorMessages);
      }
      Order order =  orderService.createOrder(orderDTO);
      return ResponseEntity.ok(order);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
  @GetMapping("/user/{user_id}")
  public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId){
    try {
      List<Order> orders = orderService.findOrderByUserId(userId);
      return ResponseEntity.ok(orders);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
  @GetMapping("/{id}")
  public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long Id){
    try {
      Order existingOrder = orderService.getOrder(Id);
      return ResponseEntity.ok(existingOrder);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
  @PutMapping("/{id}")
  public ResponseEntity<?> updateOrder(@Valid @PathVariable Long id, @Valid @RequestBody OrderDTO orderDTO) {
    try {
      Order order = orderService.updateOrder(id, orderDTO);
      return ResponseEntity.ok(order );
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOrder(@Valid @PathVariable Long id)
  {
    orderService.deleteOrder(id);
    return ResponseEntity.ok("Order deleted successfully");
  }
}

