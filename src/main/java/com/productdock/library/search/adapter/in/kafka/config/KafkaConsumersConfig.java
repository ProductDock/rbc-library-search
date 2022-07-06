package com.productdock.library.search.adapter.in.kafka.config;

import com.productdock.library.search.adapter.in.kafka.messages.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Configuration
@EnableKafka
public class KafkaConsumersConfig extends BaseKafkaConsumersConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InsertBookMessage> insertBookMessageKafkaListenerContainerFactory() {
        return createConcurrentKafkaListenerContainerFactory(InsertBookMessage.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RentalMessage> rentalMessageKafkaListenerContainerFactory() {
        return createConcurrentKafkaListenerContainerFactory(RentalMessage.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookAvailabilityMessage> bookAvailabilityMessageKafkaListenerContainerFactory() {
        return createConcurrentKafkaListenerContainerFactory(BookAvailabilityMessage.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookRatingMessage> bookRatingMessageKafkaListenerContainerFactory() {
        return createConcurrentKafkaListenerContainerFactory(BookRatingMessage.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookRecommendationMessage> bookRecommendationMessageKafkaListenerContainerFactory() {
        return createConcurrentKafkaListenerContainerFactory(BookRecommendationMessage.class);
    }

}
