package com.productdock.library.search.data.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.library.search.cosumer.messages.InsertBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class KafkaTestProducer {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, InsertBook payload) throws JsonProcessingException {
        String message =  OBJECT_MAPPER.writeValueAsString(payload);
        kafkaTemplate.send(topic, message);
    }
}
