package com.productdock.library.search;


import com.productdock.library.search.data.provider.KafkaTestProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static com.productdock.library.search.data.provider.InsertBookMessageMother.defaultInsertBookMessageBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
public class IndexingNewBookTest extends IntegrationTestBase {

    @Autowired
    private KafkaTestProducer producer;

    @Autowired
    private BookDocumentRepository bookDocumentRepository;

    @Value("${spring.kafka.topic.insert-book-topic}")
    private String topic;

    @Test
    void shouldSaveBookIndex_whenMessageReceived() {
        var insertBook = defaultInsertBookMessageBuilder().id("123").author("Book author").build();

        producer.send(topic, insertBook);

        await()
                .atMost(Duration.ofSeconds(20))
                .until(() -> bookDocumentRepository.findById("123").isPresent());

        var insertedBookDocument = bookDocumentRepository.findById("123").get();

        assertThat(insertedBookDocument).isNotNull();
        assertThat(insertedBookDocument.author).isEqualTo("Book author");
    }
}
