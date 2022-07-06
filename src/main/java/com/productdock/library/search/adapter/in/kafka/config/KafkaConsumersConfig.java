package com.productdock.library.search.adapter.in.kafka.config;

import com.productdock.library.search.adapter.in.kafka.messages.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumersConfig {

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
    public ConcurrentKafkaListenerContainerFactory<String, InsertBookMessage> insertBookMessageKafkaListenerContainerFactory() {
        KafkaListenerAssembler<InsertBookMessage> assembler = new KafkaListenerAssembler<>();
        return assembler.create(InsertBookMessage.class, getProps());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RentalMessage> rentalMessageKafkaListenerContainerFactory() {
        KafkaListenerAssembler<RentalMessage> assembler = new KafkaListenerAssembler<>();
        return assembler.create(RentalMessage.class, getProps());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookAvailabilityMessage> bookAvailabilityMessageKafkaListenerContainerFactory() {
        KafkaListenerAssembler<BookAvailabilityMessage> assembler = new KafkaListenerAssembler<>();
        return assembler.create(BookAvailabilityMessage.class, getProps());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookRatingMessage> bookRatingMessageKafkaListenerContainerFactory() {
        KafkaListenerAssembler<BookRatingMessage> assembler = new KafkaListenerAssembler<>();
        return assembler.create(BookRatingMessage.class, getProps());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookRecommendationMessage> bookRecommendationMessageKafkaListenerContainerFactory() {
        KafkaListenerAssembler<BookRecommendationMessage> assembler = new KafkaListenerAssembler<>();
        return assembler.create(BookRecommendationMessage.class, getProps());
    }

}
