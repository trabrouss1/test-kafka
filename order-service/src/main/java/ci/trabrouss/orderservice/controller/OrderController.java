package ci.trabrouss.orderservice.controller;

import ci.trabrouss.orderservice.dto.OrderEvent;
import ci.trabrouss.orderservice.dto.OrderRequest;
import ci.trabrouss.orderservice.producer.OrderProducer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.ACCEPTED;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

  private final OrderProducer orderProducer;

  @PostMapping
  public ResponseEntity<Map<String, String>> create(@Valid @RequestBody OrderRequest request) {

    String uuid = UUID.randomUUID().toString();
    OrderEvent orderEvent = OrderEvent.of(uuid, request.customerId(),
      request.productName(), request.quantity(), request.totalAmount());

    orderProducer.sendOrderEvent(orderEvent);
    log.info("Order created successfully: {}", orderEvent.orderId());

    return ResponseEntity
      .status(ACCEPTED)
      .body(Map.of(
        "orderId", orderEvent.orderId(),
        "status", "CREATED"
        ,"message", "Order placed successfully and under processing"
      ));
  }

}
