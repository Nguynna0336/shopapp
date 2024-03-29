package com.example.shopapp.Controllers;

import com.example.shopapp.Exceptions.DataNotFoundException;
import com.example.shopapp.Models.OrderDetail;
import com.example.shopapp.Responses.OrderDetailResponse;
import com.example.shopapp.Services.OrderDetailService;
import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orderdetails")
@RequiredArgsConstructor
public class OrderDetailController {
  private final OrderDetailService orderDetailService;
  @PostMapping("")
  public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO)
  {
    try {
      OrderDetail orderDetail =  orderDetailService.createOrderDetail(orderDetailDTO);
      return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(orderDetail));
    } catch (DataNotFoundException e) {
      throw new RuntimeException(e);
    }

  }
  @GetMapping("/{id}")
  public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long Id) throws DataNotFoundException {
    OrderDetail orderDetail = orderDetailService.getOrderDetail(Id);
    return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(orderDetail));
//    return ResponseEntity.ok(orderDetail);
  }
  @GetMapping("/order/{order_id}")
  private ResponseEntity<?> getOrderDetails( @Valid @PathVariable("order_id") Long orderID) {
    List<OrderDetail> orderDetails =  orderDetailService.findByOrderId(orderID);
    List<OrderDetailResponse> orderDetailResponses = orderDetails.stream().map(OrderDetailResponse::fromOrderDetail ).toList();
    return ResponseEntity.ok(orderDetailResponses);
  }
  @PutMapping("/{id}")
  public ResponseEntity<?> updateOrder(@Valid @PathVariable Long id, @Valid @RequestBody OrderDetailDTO orderDetailDTO) {
    try {
      OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
      return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
    } catch (DataNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") Long id) {
    orderDetailService.deleteById(id);
    return ResponseEntity.ok().body("Delete Order Detail with id: " + id + " successfully");
  }
}
