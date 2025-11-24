package com.vaishnavs.javademos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vaishnavs.javademos.dto.BadRequestDto;
import com.vaishnavs.javademos.exception.BadRequestException;
import com.vaishnavs.javademos.exception.ResourceNotFoundException;
import com.vaishnavs.javademos.exception.UnauthorizedException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/test-exception")
public class TestExceptionController {

  @GetMapping("/resource-not-found")
  public ResponseEntity<String> throwResourceNotFoundException() {
    throw new ResourceNotFoundException("Resource you are looking for is not found.");
  }

  @GetMapping("/bad-request")
  public ResponseEntity<String> throwBadRequestException() {
    throw new BadRequestException("This is a bad request.");
  }

  @PostMapping("/bad-request")
  public ResponseEntity<String> throwBadRequestExceptionOnPost(@Valid @RequestBody BadRequestDto badRequestDto) {
    return ResponseEntity.ok("Valid request received with message: " + badRequestDto.getMessage());
  }

  @GetMapping("/admin")
  public ResponseEntity<String> throwUnauthorizedException() {
    throw new UnauthorizedException("You are not authorized to access this resource.");
  }

  @GetMapping("/generic-error")
  public ResponseEntity<String> throwGenericException() throws Exception {
    throw new Exception("This is a generic exception.");
  }

}
