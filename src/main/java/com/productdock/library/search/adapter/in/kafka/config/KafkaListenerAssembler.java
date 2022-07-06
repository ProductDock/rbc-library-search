package com.productdock.library.search.adapter.in.kafka.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

public class KafkaListenerAssembler<V> {

    public ConcurrentKafkaListenerContainerFactory<String, V> create(Class<V> messageClass, Map<String, Object> props) {
        ConcurrentKafkaListenerContainerFactory<String, V> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createConsumerFactory(messageClass, props));
        return factory;
    }

    private DefaultKafkaConsumerFactory<String, V> createConsumerFactory(Class<V> messageClass, Map<String, Object> props) {
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(messageClass));
    }
}
