package com.productdock.library.search.integration;

import com.productdock.library.search.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.library.search.data.provider.KafkaTestProducer;
import com.productdock.library.search.adapter.in.kafka.messages.InsertBookMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class IndexingNewBookTest extends IntegrationTestBase {

    @Autowired
    private KafkaTestProducer producer;

    @Autowired
    private BookPersistenceOutPort bookDocumentRepository;

    @Value("${spring.kafka.topic.insert-book}")
    private String topic;

    @Test
    void shouldSaveBookIndex_whenMessageReceived() throws Exception {
        var bookTopic =
                InsertBookMessage.Topic.builder().id("1").name("::topic::").build();
        var insertBook = InsertBookMessage.builder().bookId("123").title("::title::").cover("::cover::").author("Book author").topic(bookTopic).build();

        producer.send(topic, insertBook);

        await()
                .atMost(Duration.ofSeconds(5))
                .until(() -> bookDocumentRepository.findById("123").isPresent());

        var insertedBookDocument = bookDocumentRepository.findById("123").get();

        assertThat(insertedBookDocument).isNotNull();
        assertThat(insertedBookDocument.getAuthor()).isEqualTo("Book author");
    }
}
