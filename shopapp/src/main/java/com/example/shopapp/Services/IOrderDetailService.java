package com.example.shopapp.Services;

import com.example.shopapp.Exceptions.DataNotFoundException;
import com.example.shopapp.Models.OrderDetail;
import com.example.shopapp.dtos.OrderDetailDTO;

import java.util.List;

public interface IOrderDetailService {
  OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
  OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
  OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

  void deleteById(Long id);

  List<OrderDetail> findByOrderId(Long orderId);
}
