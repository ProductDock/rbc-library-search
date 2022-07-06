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

import static com.productdock.library.search.data.provider.BookMother.defaultBookBuilder;
import static com.productdock.library.search.data.provider.messages.BookRatingMessageMother.bookRatingBuilder;
import static org.assertj.core.api.Assertions.assertThat;
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
        var bookRated = bookRatingBuilder().bookId(BOOK_ID).rating(3.5).ratingsCount(2).build();

        producer.send(bookRatingTopic, bookRated);

        await()
                .atMost(Duration.ofSeconds(5))
                .ignoreException(NullPointerException.class)
                .until(bookRatingIsChanged());

        var bookDocument = bookRepository.findById(BOOK_ID).get();

        assertThat(bookDocument.getRating().getCount()).isEqualTo(2);
        assertThat(bookDocument.getRating().getScore()).isEqualTo(3.5);
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
