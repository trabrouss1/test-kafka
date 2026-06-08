package ci.trabrouss.orderservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderRequest(

  @NotBlank(message = "Customer ID cannot be blank")
  String customerId,

  @NotBlank(message = "product name cannot be blank")
  String productName,

  @Min(value = 1, message = "Customer ID cannot be blank")
  int quantity,

  @NotNull(message = "Customer ID cannot be null")
  @DecimalMin(value = "0.01", message = "Total amount must be at least 0.01")
  BigDecimal totalAmount
) {}
