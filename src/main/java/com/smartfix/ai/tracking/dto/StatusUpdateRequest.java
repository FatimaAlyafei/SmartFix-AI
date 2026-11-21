package com.smartfix.ai.tracking.dto;


import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record StatusUpdateRequest(
 @NotBlank String status,
 String resolution,
 @DecimalMin("0.0") BigDecimal totalCost
) {}
