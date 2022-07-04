package com.productdock.library.search.data.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class KafkaTestProducer {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @SneakyThrows
    public SendResult<String, String> send(String topic, Object payload) {
        String message = OBJECT_MAPPER.writeValueAsString(payload);
        ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(topic, message);
        ListenableFutureCallback<? super SendResult<String, String>> callb = new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("PRODUCER FAILED - topic {} message {} ", topic, message);
                log.error("PRODUCER FAILED", ex.getStackTrace());
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("PRODUCER SUCCESS - topic {} message {} ", topic, message);
            }
        };
        send.addCallback(callb);
        return send.get();
    }
}
