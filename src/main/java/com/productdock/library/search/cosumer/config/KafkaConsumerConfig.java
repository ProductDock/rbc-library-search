package com.productdock.library.search.cosumer.config;

import com.productdock.library.search.cosumer.messages.InsertBook;
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

    private String groupId = "rbc-library";

    @Bean
    public ConsumerFactory<String, InsertBook> insertBookMessageConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                groupId);
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(InsertBook.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, InsertBook>
    insertBookMessageKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, InsertBook> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(insertBookMessageConsumerFactory());
        return factory;
    }
}
