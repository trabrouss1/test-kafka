package ci.trabrouss.orderservice.producer;

import ci.trabrouss.orderservice.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProducer {

  @Value("${kafka.topics.orders}")
  private String ordersTopic;

  private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

  public CompletableFuture<SendResult<String, OrderEvent>> sendOrderEvent(OrderEvent orderEvent) {
    log.info("Sending order event: {} to topic {}", orderEvent,  ordersTopic);

//    CompletableFuture<SendResult<String, OrderEvent>> future = kafkaTemplate.send(ordersTopic, orderEvent);
    var future = kafkaTemplate.send(ordersTopic, orderEvent);

    future.whenComplete((result, exception) -> {
      if (exception != null) {
        log.error("Error sending order event: {}", exception.getMessage());
      } else {
        var recordMetadata = result.getRecordMetadata();
        log.info("Order event sent successfully : offset = {}, partition = {}", recordMetadata.offset(), recordMetadata.partition());
      }
    });

    return future;
  }

}
