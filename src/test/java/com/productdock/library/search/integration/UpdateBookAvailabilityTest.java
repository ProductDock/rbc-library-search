package com.productdock.library.search.integration;

import com.productdock.library.search.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.library.search.data.provider.KafkaTestProducer;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.concurrent.Callable;

import static com.productdock.library.search.data.provider.messages.BookAvailabilityMessageMother.bookAvailabilityBuilder;
import static com.productdock.library.search.data.provider.BookMother.defaultBookBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Slf4j
class UpdateBookAvailabilityTest extends IntegrationTestBase {

    private static final int AVAILABLE_BOOK_COUNT = 22;

    @Autowired
    private KafkaTestProducer producer;

    @Autowired
    private BookPersistenceOutPort bookRepository;

    private final String BOOK_ID = "1";

    @Value("${spring.kafka.topic.book-availability}")
    private String bookAvailabilityTopic;

    @Test
    void shouldUpdateBookAvailability_WhenBookAvailabilityMessageReceived() {
        givenBookWithId(BOOK_ID);
        var bookAvailabilityChanged = bookAvailabilityBuilder()
                .bookId(BOOK_ID)
                .availableBookCount(AVAILABLE_BOOK_COUNT).build();

        producer.send(bookAvailabilityTopic, bookAvailabilityChanged);

        await()
                .atMost(Duration.ofSeconds(5))
                .ignoreException(NullPointerException.class)
                .until(availableBookCountIsChanged());

        var bookDocument = bookRepository.findById(BOOK_ID).get();

        assertThat(bookDocument.getRentalState().getAvailableBooksCount()).isEqualTo(AVAILABLE_BOOK_COUNT);
    }

    @NonNull
    private Callable<Boolean> availableBookCountIsChanged() {
        return () -> bookRepository.findById(BOOK_ID).get().getRentalState().getAvailableBooksCount() != 0;
    }

    private void givenBookWithId(String bookId) {
        var book = defaultBookBuilder().id(bookId).build();
        bookRepository.save(book);
    }

}
