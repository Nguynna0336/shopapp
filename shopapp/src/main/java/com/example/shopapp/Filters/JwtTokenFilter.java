package com.example.shopapp.Filters;

import com.example.shopapp.Components.JwtTokenUtil;
import com.example.shopapp.Configurations.SecurityConfig;
import com.example.shopapp.Models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
  private final UserDetailsService userDetailsService;
  private final JwtTokenUtil jwtTokenUtil;
  @Value("${api.prefix}")
  private String apiPrefix;
  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {
    try {
      if(isBypassToken(request)) {
        filterChain.doFilter(request, response);
        return;
      }
      final String authHeader = request.getHeader("Authorization");
      if (authHeader == null && !authHeader.startsWith("Bearer ")) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        return;
      }
        final String token = authHeader.substring(7);
        final String phoneNumber =  jwtTokenUtil.extractPhoneNumber(token);
        if(phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber);
          if (jwtTokenUtil.validateToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
              );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          }
        }

      filterChain.doFilter(request, response);
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
  }
  private boolean isBypassToken(@NonNull HttpServletRequest request) {
    final List<Pair<String, String>> byPassTokens = Arrays.asList(
      Pair.of(String.format("%s/products", apiPrefix),  "GET"),
      Pair.of(String.format("%s/categories", apiPrefix),  "GET"),
      Pair.of(String.format("%s/users/register", apiPrefix),  "POST"),
      Pair.of(String.format("%s/users/login", apiPrefix),  "POST"));
    for(Pair<String, String> bypassToken: byPassTokens) {
      if (request.getServletPath().contains(bypassToken.getLeft()) && request.getMethod().equals(bypassToken.getRight())) {
        return true;
      }
    }
    return false;
  }
}
