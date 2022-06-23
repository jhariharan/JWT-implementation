package com.carlease.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.carlease.jwt.entity.JwtRequest;
import com.carlease.jwt.entity.JwtResponse;
import com.carlease.jwt.service.UserService;
import com.carlease.jwt.util.JWTUtility;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AuthenticationController {

  @Autowired
  private JWTUtility jwtUtility;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserService userService;

  @GetMapping("/")
  public String helloWorld() {
    return "hi there!";
  }

  @PostMapping("/authenticate")
  public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
    try {
      log.info("inside the try block,calling the authenticate method");
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPasword()));

    } catch (BadCredentialsException be) {
      log.info("inside the catch block,something went wrong with the credentials");
      throw new Exception("invalid credentials", be);
    }
    final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
    final String token = jwtUtility.generateToken(userDetails);
    return new JwtResponse(token);
  }
}
