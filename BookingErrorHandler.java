package com.smartfix.ai.booking.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestControllerAdvice(assignableTypes = BookingController.class)
public class BookingErrorHandler {
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Map<String,String>> handle(ResponseStatusException ex) {
    return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()==null ? "Request failed" : ex.getReason()));
  }
}
