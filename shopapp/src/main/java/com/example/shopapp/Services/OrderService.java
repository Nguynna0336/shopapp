package com.example.shopapp.Services;

import com.example.shopapp.Exceptions.DataNotFoundException;
import com.example.shopapp.Models.Order;
import com.example.shopapp.Models.OrderStatus;
import com.example.shopapp.Models.User;
import com.example.shopapp.Repositories.OrderRepository;
import com.example.shopapp.Repositories.UserRepository;
import com.example.shopapp.dtos.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
  private final UserRepository userRepository;
  private final OrderRepository orderRepository;
  private final ModelMapper modelMapper;
  @Override
  public Order createOrder(OrderDTO orderDTO) throws Exception {
    User user = userRepository
      .findById(orderDTO.getUserID())
      .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + orderDTO.getUserID()));
    modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));
    Order order = new Order();
    modelMapper.map(orderDTO,order);
    order.setUser(user);
    order.setOrderDate(new Date());
    order.setStatus(OrderStatus.PENDING);
    LocalDate shippingDate = orderDTO.getShippingDate() == null? LocalDate.now(): orderDTO.getShippingDate() ;
    if(shippingDate.isBefore(LocalDate.now())) {
      throw new DataNotFoundException("Date must be at least today");
    }
    order.setShippingDate(shippingDate);
    order.setActive(true);
    orderRepository.save(order);

    return order;
  }

  @Override
  public Order getOrder(Long id) {
    return orderRepository.findById(id).orElse(null);
  }

  @Override
  public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
    Order existingOrder = orderRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Cannot find order with id: " + id));
    User existingUser = userRepository.findById(orderDTO.getUserID()).orElseThrow(() -> new DataNotFoundException("Cannot find user with id: " + id));
    modelMapper.typeMap(OrderDTO.class, Order.class).addMappings(mapper -> mapper.skip(Order::setId));
    Order order = new Order();
    modelMapper.map(orderDTO, order);
    order.setUser(existingUser);
    return orderRepository.save(order);
  }

  @Override
  public void deleteOrder(Long id) {
    Order order = orderRepository.findById(id).orElse(null);
    if(order != null) {
      order.setActive(false);
      orderRepository.save(order);
    }
  }

  @Override
  public List<Order> findOrderByUserId(Long userId) {
    return orderRepository.findByUserId(userId);
  }
}
