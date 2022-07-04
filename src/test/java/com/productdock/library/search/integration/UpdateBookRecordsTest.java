package com.productdock.library.search.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.productdock.library.search.adapter.in.kafka.KafkaConsumer;
import com.productdock.library.search.adapter.in.kafka.messages.RentalMessage;
import com.productdock.library.search.application.port.out.persistence.BookDocumentPersistenceOutPort;
import com.productdock.library.search.data.provider.KafkaTestProducer;
import com.productdock.library.search.domain.BookStatus;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.KafkaClient;
import org.apache.kafka.clients.NetworkClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Callable;

import static com.productdock.library.search.data.provider.BookAvailabilityMessageMother.defaultBookAvailabilityMessageBuilder;
import static com.productdock.library.search.data.provider.BookMother.defaultBookBuilder;
import static com.productdock.library.search.data.provider.BookRatingMessageMother.defaultBookRatingMessage;
import static com.productdock.library.search.data.provider.BookRecommendationMessageMother.defaultBookRecommendationMessageBuilder;
import static com.productdock.library.search.data.provider.RentalMessageMother.defaultRentalMessageBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Slf4j
class UpdateBookRecordsTest extends IntegrationTestBase {

    @Autowired
    private KafkaTestProducer producer;

    @Autowired
    private BookDocumentPersistenceOutPort bookDocumentRepository;

    private final String BOOK_ID = "1";

    @Value("${spring.kafka.topic.book-status}")
    private String bookStatusTopic;

    @Value("${spring.kafka.topic.book-availability}")
    private String bookAvailabilityTopic;

    @Value("${spring.kafka.topic.book-rating}")
    private String bookRatingTopic;

    @Value("${spring.kafka.topic.book-recommendation}")
    private String bookRecommendationTopic;

    @Test
    void shouldUpdateBookRecords_WhenRentalMessageReceived() throws InterruptedException {
        log.info("Started TEST shouldUpdateBookRecords_WhenRentalMessageReceived");
        givenBookWithId();
        var rentalMessageRecord = RentalMessage.Record.builder().patron("email").status(BookStatus.RENTED).build();
        var rentalMessage = defaultRentalMessageBuilder()
                .rentalRecord(rentalMessageRecord).build();

        producer.send(bookStatusTopic, rentalMessage);
        log.debug("AFTER Producer send");

        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        threadSet.forEach((thread -> log.debug(thread.getName())));
        Thread.sleep(2000);
        Set<Thread> threadSetAfter = Thread.getAllStackTraces().keySet();
        threadSetAfter.forEach((thread -> log.debug(thread.getName())));

        await()
                .atMost(Duration.ofSeconds(5))
                .ignoreException(NullPointerException.class)
                .until(newRecordIsPresent());

        var bookDocument = bookDocumentRepository.findById(BOOK_ID).get();

        log.info("ENDED TEST shouldUpdateBookRecords_WhenRentalMessageReceived");
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
    void shouldUpdateBookAvailability_WhenBookAvailabilityMessageReceived() {
        log.info("Started TEST shouldUpdateBookAvailability_WhenBookAvailabilityMessageReceived");
        givenBookWithId();
        int availableBookCount = 2;
        var bookAvailabilityMessage = defaultBookAvailabilityMessageBuilder()
                .availableBookCount(availableBookCount).build();

        producer.send(bookAvailabilityTopic, bookAvailabilityMessage);
        log.debug("AFTER Producer send");

        await()
                .atMost(Duration.ofSeconds(5))
                .ignoreException(NullPointerException.class)
                .until(availableBookCountIsChanged());

        var bookDocument = bookDocumentRepository.findById(BOOK_ID).get();

        log.info("ENDED TEST shouldUpdateBookAvailability_WhenBookAvailabilityMessageReceived");
        assertThat(bookDocument.getRentalState().getAvailableBooksCount()).isEqualTo(availableBookCount);
    }

    @NonNull
    private Callable<Boolean> availableBookCountIsChanged() {
        return () -> bookDocumentRepository.findById(BOOK_ID).get().getRentalState().getAvailableBooksCount() != 0;
    }

    @Test
    void shouldUpdateBookRating_WhenBookRatingMessageReceived() {
        log.info("Started TEST shouldUpdateBookRating_WhenBookRatingMessageReceived");
        givenBookWithId();
        var bookRatingMessage = defaultBookRatingMessage();

        producer.send(bookRatingTopic, bookRatingMessage);
        log.debug("AFTER Producer send");

        await()
                .atMost(Duration.ofSeconds(5))
                .ignoreException(NullPointerException.class)
                .until(bookRatingIsChanged());

        var bookDocument = bookDocumentRepository.findById(BOOK_ID).get();

        log.info("ENDED TEST shouldUpdateBookRating_WhenBookRatingMessageReceived");
        assertThat(bookDocument.getRating().getCount()).isEqualTo(bookRatingMessage.getRatingsCount());
        assertThat(bookDocument.getRating().getScore()).isEqualTo(bookRatingMessage.getRating());
    }

    @NonNull
    private Callable<Boolean> bookRatingIsChanged() {
        return () -> bookDocumentRepository.findById(BOOK_ID).get().getRating().getCount() != 0;
    }

    @Test
    void shouldUpdateBookRecommendations_WhenBookRecommendedMessageReceived() {
        log.info("Started TEST shouldUpdateBookRecommendations_WhenBookRecommendedMessageReceived");
        givenBookWithId();
        var bookRecommendationMessage = defaultBookRecommendationMessageBuilder().recommended(true).build();

        producer.send(bookRecommendationTopic, bookRecommendationMessage);
        log.debug("AFTER Producer send");

        await()
                .atMost(Duration.ofSeconds(5))
                .ignoreException(NullPointerException.class)
                .until(bookRecommendationsIsChanged());

        var bookDocument = bookDocumentRepository.findById(BOOK_ID).get();

        log.info("ENDED TEST shouldUpdateBookRecommendations_WhenBookRecommendedMessageReceived");
        assertThat(bookDocument.isRecommended()).isTrue();
    }

    @NonNull
    private Callable<Boolean> bookRecommendationsIsChanged() {
        return () -> bookDocumentRepository.findById(BOOK_ID).get().isRecommended();
    }

    private void givenBookWithId() {
        var book = defaultBookBuilder().id(BOOK_ID).build();
        bookDocumentRepository.save(book);
    }
}
