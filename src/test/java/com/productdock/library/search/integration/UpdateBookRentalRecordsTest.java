package com.productdock.library.search.integration;

import com.productdock.library.search.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.library.search.data.provider.KafkaTestProducer;
import com.productdock.library.search.domain.BookStatus;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.concurrent.Callable;

import static com.productdock.library.search.data.provider.BookMother.defaultBookBuilder;
import static com.productdock.library.search.data.provider.messages.RentalMessageMother.rentalMessageBuilder;
import static com.productdock.library.search.data.provider.messages.RentalRecordMessageMother.rentedRecordBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@Slf4j
class UpdateBookRentalRecordsTest extends IntegrationTestBase {

    @Autowired
    private KafkaTestProducer producer;

    @Autowired
    private BookPersistenceOutPort bookRepository;

    private final String BOOK_ID = "1";

    @Value("${spring.kafka.topic.book-status}")
    private String bookStatusTopic;

    @Test
    void shouldUpdateBookRecords_WhenRentalMessageReceived() {
        givenBookWithId(BOOK_ID);
        var bookRented = rentalMessageBuilder()
            .bookId(BOOK_ID)
            .rentalRecord(rentedRecordBuilder().patron("patron@email").build())
            .build();

        producer.send(bookStatusTopic, bookRented);

        await()
                .atMost(Duration.ofSeconds(5))
                .ignoreException(NullPointerException.class)
                .until(newRecordIsPresent());

        var bookDocument = bookRepository.findById(BOOK_ID).get();

        assertThat(bookDocument.getRentalState().getRecords()).hasSize(1);
        assertThat(bookDocument.getRentalState().getRecords())
                .extracting("email", "status")
                .containsExactly(tuple("patron@email", BookStatus.RENTED));
    }

    private void givenBookWithId(String bookId) {
        var book = defaultBookBuilder().id(bookId).build();
        bookRepository.save(book);
    }

    @NonNull
    private Callable<Boolean> newRecordIsPresent() {
        return () -> !bookRepository.findById(BOOK_ID).get().getRentalState().getRecords().isEmpty();
    }

}
