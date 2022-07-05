package com.productdock.library.search.adapter.in.kafka.config;

import com.productdock.library.search.adapter.in.kafka.messages.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

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

    @Bean
    public ConsumerFactory<String, InsertBookMessage> insertBookMessageConsumerFactory() {
        Map<String, Object> props = getProps();
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(InsertBookMessage.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InsertBookMessage>
    insertBookMessageKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, InsertBookMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(insertBookMessageConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, RentalMessage> rentalMessageConsumerFactory() {
        Map<String, Object> props = getProps();
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(RentalMessage.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RentalMessage> rentalMessageKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, RentalMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(rentalMessageConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BookAvailabilityMessage> bookAvailabilityConsumerFactory() {
        Map<String, Object> props = getProps();
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(BookAvailabilityMessage.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookAvailabilityMessage> bookAvailabilityMessageKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, BookAvailabilityMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bookAvailabilityConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BookRatingMessage> bookRatingConsumerFactory() {
        Map<String, Object> props = getProps();
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(BookRatingMessage.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookRatingMessage> bookRatingMessageKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, BookRatingMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bookRatingConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BookRecommendationMessage> bookRecommendationConsumerFactory() {
        Map<String, Object> props = getProps();
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(BookRecommendationMessage.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookRecommendationMessage> bookRecommendationMessageKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, BookRecommendationMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bookRecommendationConsumerFactory());
        return factory;
    }
}
