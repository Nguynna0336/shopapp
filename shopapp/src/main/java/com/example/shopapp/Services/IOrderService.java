package com.example.shopapp.Services;

import com.example.shopapp.Exceptions.DataNotFoundException;
import com.example.shopapp.Models.Order;
import com.example.shopapp.dtos.OrderDTO;

import java.util.List;

public interface IOrderService {
  Order createOrder(OrderDTO orderDTO) throws Exception;
  Order getOrder(Long id);
  Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;
  void deleteOrder(Long id);
  List<Order> findOrderByUserId(Long userId);
}
