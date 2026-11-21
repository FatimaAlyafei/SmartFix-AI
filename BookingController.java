package com.smartfix.ai.booking.controller;

import com.smartfix.ai.booking.service.BookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BookingController {
  private final BookingService service;
  public BookingController(BookingService service) { this.service = service; }
  public record BookRequest(@NotNull Long requestId, @NotNull Long technicianId,
      @NotNull Long slotId, @NotNull @DecimalMin("0.0") BigDecimal estimatedCost) {}
  @GetMapping("/technicians/recommended")
  public List<Map<String,Object>> recommended(@RequestParam long requestId) { return service.recommended(requestId); }
  @GetMapping("/technicians/{id}/slots")
  public List<Map<String,Object>> slots(@PathVariable long id) { return service.slots(id); }
  @PostMapping("/appointments")
  public Map<String,Object> book(@Valid @RequestBody BookRequest request) { return service.book(request); }
  @PatchMapping("/appointments/{id}/accept")
  public Map<String,Object> accept(@PathVariable long id) { return service.accept(id); }
  @PatchMapping("/appointments/{id}/cancel")
  public Map<String,Object> cancel(@PathVariable long id) { return service.cancel(id); }
}
