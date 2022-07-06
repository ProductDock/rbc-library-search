package com.productdock.library.search.adapter.in.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseKafkaConsumersConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${kafka-consumer-factory.group-id}")
    private String consumerGroup;

    @Value("${kafka-consumer-factory.auto-offset-reset}")
    private String autoOffsetReset;

    private Map<String, Object> getProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                consumerGroup);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        return props;
    }

    protected ConcurrentKafkaListenerContainerFactory createConcurrentKafkaListenerContainerFactory(Class messageClass) {
        ConcurrentKafkaListenerContainerFactory factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createConsumerFactory(messageClass));
        return factory;
    }

    private DefaultKafkaConsumerFactory createConsumerFactory(Class messageClass) {
        Map<String, Object> props = getProps();
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(messageClass));
    }

}
