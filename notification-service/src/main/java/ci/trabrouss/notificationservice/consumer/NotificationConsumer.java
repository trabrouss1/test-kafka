package ci.trabrouss.notificationservice.consumer;

import ci.trabrouss.notificationservice.dto.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationConsumer {

  @KafkaListener(topics = "${kafka.topics.orders}", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
  public void consume(ConsumerRecord<String, OrderEvent> consumerRecord,
                      @Header(KafkaHeaders.OFFSET) long offset,
                      @Header(KafkaHeaders.RECEIVED_PARTITION) int partition
  ) {
    var event = consumerRecord.value();

    log.info("""
                New message received:
                Topic: {}
                Partition: {}
                Offset: {}
                Key: {}
                OrderId: {}
                Customer: {}
                Product: {}
                Amount: {}
                Status: {}
                """, consumerRecord.topic(), partition, offset, consumerRecord.key(), event.orderId(), event.customerId(), event.productName(), event.totalAmount(), event.status());

  }

}
