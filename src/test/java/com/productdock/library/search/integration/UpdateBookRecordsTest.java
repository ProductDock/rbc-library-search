package com.productdock.library.search.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.library.search.book.BookDocumentRepository;
import com.productdock.library.search.book.BookStatus;
import com.productdock.library.search.data.provider.KafkaTestProducer;
import com.productdock.library.search.kafka.consumer.messages.RentalMessage;
import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.concurrent.Callable;

import static com.productdock.library.search.data.provider.BookDocumentMother.defaultBookDocumentBuilder;
import static com.productdock.library.search.data.provider.BookAvailabilityMessageMother.defaultBookAvailabilityMessageBuilder;
import static com.productdock.library.search.data.provider.RentalMessageMother.defaultRentalMessageBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class UpdateBookRecordsTest extends IntegrationTestBase {

    @Autowired
    private KafkaTestProducer producer;

    @Autowired
    private BookDocumentRepository bookDocumentRepository;

    private final String BOOK_ID = "1";
    private final int AVAILABLE_BOOK_COUNT = 2;

    @Value("${spring.kafka.topic.book-status}")
    private String bookStatusTopic;

    @Value("${spring.kafka.topic.book-availability}")
    private String bookAvailabilityTopic;

    @Test
    void shouldUpdateBookRecords_WhenRentalMessageReceived() throws JsonProcessingException {
        givenBookWithId();
        var rentalMessage = defaultRentalMessageBuilder()
                .record(new RentalMessage.Record("email", BookStatus.RENTED)).build();

        producer.send(bookStatusTopic, rentalMessage);

        await()
                .atMost(Duration.ofSeconds(5))
                .ignoreException(NullPointerException.class)
                .until(newRecordIsPresent());

        var bookDocument = bookDocumentRepository.findById(BOOK_ID).get();

        assertThat(bookDocument.getRentalState().getRecords()).hasSize(1);
        assertThat(bookDocument.getRentalState().getRecords())
                .extracting("email", "status")
                .containsExactly(tuple("email", BookStatus.RENTED));
    }

    @NonNull
    private Callable<Boolean> newRecordIsPresent() {
        return () -> !bookDocumentRepository.findById(BOOK_ID).get().getRentalState().getRecords().isEmpty();
    }

    @Test
    void shouldUpdateBookAvailability_WhenBookAvailabilityMessageReceived() throws JsonProcessingException {
        givenBookWithId();
        var bookAvailabilityMessage = defaultBookAvailabilityMessageBuilder()
                .availableBookCount(AVAILABLE_BOOK_COUNT).build();

        producer.send(bookAvailabilityTopic, bookAvailabilityMessage);

        await()
                .atMost(Duration.ofSeconds(5))
                .ignoreException(NullPointerException.class)
                .until(availableBookCountIsChanged());

        var bookDocument = bookDocumentRepository.findById(BOOK_ID).get();

        assertThat(bookDocument.getRentalState().getAvailableBooksCount()).isEqualTo(AVAILABLE_BOOK_COUNT);
    }

    @NonNull
    private Callable<Boolean> availableBookCountIsChanged() {
        return () -> bookDocumentRepository.findById(BOOK_ID).get().getRentalState().getAvailableBooksCount() != 0;
    }

    private void givenBookWithId() {
        var book = defaultBookDocumentBuilder().bookId(BOOK_ID).build();
        bookDocumentRepository.save(book);
    }
}
