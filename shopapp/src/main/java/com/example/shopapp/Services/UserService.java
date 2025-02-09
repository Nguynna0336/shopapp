package com.example.shopapp.Services;

import com.example.shopapp.Components.JwtTokenUtil;
import com.example.shopapp.Configurations.SecurityConfig;
import com.example.shopapp.Exceptions.DataNotFoundException;
import com.example.shopapp.Exceptions.PermissionDenyException;
import com.example.shopapp.Models.Role;
import com.example.shopapp.Models.User;
import com.example.shopapp.Repositories.RoleRepository;
import com.example.shopapp.Repositories.UserRepository;
import com.example.shopapp.dtos.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenUtil jwtTokenUtil;
  private final AuthenticationManager authenticationManager;
  @Override
  public User createUser(UserDTO userDTO) throws Exception {
    String phoneNumber = userDTO.getPhoneNumber();
    if(userRepository.existsByPhoneNumber(phoneNumber)) {
      throw new DataIntegrityViolationException("Phone number already exists");
    }
    Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(() -> new DataNotFoundException("Role not found"));
    if(role.getName().toUpperCase().equals(Role.ADMIN)) {
      throw new PermissionDenyException("");
    }
    User newUser = User.builder()
      .fullName(userDTO.getFullName())
      .phoneNumber(userDTO.getPhoneNumber())
      .password(userDTO.getPassword())
      .address(userDTO.getAddress())
      .dateOfBirth(userDTO.getDateOfBirth())
      .facebookAccountId(userDTO.getFacebookAccountId())
      .googleAccountId(userDTO.getGoogleAccountId())
      .build();
    newUser.setRole(role);
    if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() ==0) {
      String password = userDTO.getPassword();
      String encodedPassword = passwordEncoder.encode(password);
      newUser.setPassword(encodedPassword);
    }
    return userRepository.save(newUser);
  }

  @Override
  public String login(String phoneNumber, String password) throws DataNotFoundException {
    Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
    if(optionalUser.isEmpty()){
      throw new DataNotFoundException("Invalid phoneNumber or password");
    }
    User existingUser = optionalUser.get();
    if(existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() ==0) {
      if (!passwordEncoder.matches(password,existingUser.getPassword())){
        throw new BadCredentialsException("Wrong phone number or password");
      }
    }
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phoneNumber, password, existingUser.getAuthorities());
    authenticationManager.authenticate(authenticationToken);
    try {
      return jwtTokenUtil.generateToken(existingUser);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
