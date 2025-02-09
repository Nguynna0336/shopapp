package com.example.shopapp.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "fullname", length = 100)
  private String fullName;

  @Column(name = "email", length =  100, nullable = false)
  private String email;

  @Column(name = "phone_number", length =  100, nullable = false)
  private String phoneNumber;

  @Column(name = "address", length =  200, nullable = false)
  private String address;
  @Column(name = "note", length =  200, nullable = false)
  private String note;

  @Column(name = "order_date")
  private Date orderDate;

  @Column(name = "status")
  private String status;

  @Column(name = "total_money")
  private Integer totalMoney;

  @Column(name = "shipping_method")
  private String shippingMethod;

  @Column(name = "shipping_address")
  private String shippingAddress;

  @Column(name = "shipping_date")
  private LocalDate shippingDate;

  @Column(name = "tracking_number")
  private String trackingNumber;

  @Column(name = "payment_method")
  private String paymentMethod;

//  @Column(name = "payment_status")
//  private String paymentStatus;
//
//  @Column(name = "payment_date")
//  private Date paymentDate;

  @Column(name = "active")
  private Boolean active;
}
