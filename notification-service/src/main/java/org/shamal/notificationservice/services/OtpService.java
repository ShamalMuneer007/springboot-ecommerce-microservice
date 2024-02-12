package org.shamal.notificationservice.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OtpService {
    @KafkaListener(topics = "identity",groupId = "otp")
    public void listen(ConsumerRecord<String, String> consumerRecord) {
        String value = consumerRecord.value();
        log.info("Message received : {}",value);
    }
}
