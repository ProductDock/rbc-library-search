package com.productdock.library.search.integration;

import com.productdock.library.search.adapter.in.kafka.messages.RentalMessage;
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
class UpdateBookRatingTest extends IntegrationTestBase {

    @Autowired
    private KafkaTestProducer producer;

    @Autowired
    private BookPersistenceOutPort bookRepository;

    private final String BOOK_ID = "1";

    @Value("${spring.kafka.topic.book-rating}")
    private String bookRatingTopic;

    @Test
    void shouldUpdateBookRating_WhenBookRatingMessageReceived() {
        givenBookWithId(BOOK_ID);
        var bookRatingMessage = defaultBookRatingMessage();

        producer.send(bookRatingTopic, bookRatingMessage);

        await()
                .atMost(Duration.ofSeconds(5))
                .ignoreException(NullPointerException.class)
                .until(bookRatingIsChanged());

        var bookDocument = bookRepository.findById(BOOK_ID).get();

        assertThat(bookDocument.getRating().getCount()).isEqualTo(bookRatingMessage.getRatingsCount());
        assertThat(bookDocument.getRating().getScore()).isEqualTo(bookRatingMessage.getRating());
    }

    @NonNull
    private Callable<Boolean> bookRatingIsChanged() {
        return () -> bookRepository.findById(BOOK_ID).get().getRating().getCount() != 0;
    }

    private void givenBookWithId(String bookId) {
        var book = defaultBookBuilder().id(bookId).build();
        bookRepository.save(book);
    }

}
