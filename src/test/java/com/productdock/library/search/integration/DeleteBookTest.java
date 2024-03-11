package com.productdock.library.search.integration;

import com.productdock.library.search.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.library.search.data.provider.KafkaTestProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static com.productdock.library.search.data.provider.BookMother.defaultBookBuilder;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class DeleteBookTest extends IntegrationTestBase {

    private final String BOOK_ID = "1";

    @Autowired
    private KafkaTestProducer producer;
    @Autowired
    private BookPersistenceOutPort bookDocumentRepository;
    @Value("${spring.kafka.topic.delete-book}")
    private String topic;

    @Test
    void shouldDeleteBook_WhenMessageReceived() throws Exception {
        givenBookWithId(BOOK_ID);

        producer.send(topic, BOOK_ID);

        await()
                .atMost(Duration.ofSeconds(5))
                .until(() -> bookDocumentRepository.findById(BOOK_ID).isEmpty());

        assertThat(bookDocumentRepository.findById(BOOK_ID)).isEmpty();
    }

    private void givenBookWithId(String bookId) {
        var book = defaultBookBuilder().id(bookId).build();
        bookDocumentRepository.save(book);
    }

}
