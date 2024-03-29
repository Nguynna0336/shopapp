package com.example.shopapp.Services;

import com.example.shopapp.Exceptions.DataNotFoundException;
import com.example.shopapp.Models.User;
import com.example.shopapp.dtos.UserDTO;

public interface IUserService {
  User createUser(UserDTO userDTO) throws Exception;
  String login(String phoneNumber, String password) throws DataNotFoundException;
}
