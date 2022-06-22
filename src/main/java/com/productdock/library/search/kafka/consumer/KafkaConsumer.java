package com.productdock.library.search.kafka.consumer;

import com.productdock.library.search.book.BookMapper;
import com.productdock.library.search.book.BookService;
import com.productdock.library.search.book.InsertBookMessageMapper;
import com.productdock.library.search.kafka.consumer.messages.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public record KafkaConsumer(BookService bookService,
                            BookMapper bookMapper, InsertBookMessageMapper insertBookMessageMapper) {

    @KafkaListener(topics = "${spring.kafka.topic.insert-book}",
            containerFactory = "insertBookMessageKafkaListenerContainerFactory")
    public synchronized void listen(InsertBookMessage insertBookMessage) {
        log.debug("On topic 'insert-book' received kafka message: {}", insertBookMessage);
        bookService.save(insertBookMessageMapper.toBookDocument(insertBookMessage));
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-status}",
            containerFactory = "rentalMessageKafkaListenerContainerFactory")
    public synchronized void listen(RentalMessage rentalMessage) {
        log.debug("On topic 'book-status' received kafka message: {}", rentalMessage);
        bookService.updateBookRecords(rentalMessage);
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-availability}",
            containerFactory = "bookAvailabilityMessageKafkaListenerContainerFactory")
    public synchronized void listen(BookAvailabilityMessage bookAvailabilityMessage) {
        log.debug("On topic 'book-availability' received kafka message: {}", bookAvailabilityMessage);
        bookService.updateAvailabilityBookCount(bookAvailabilityMessage);
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-rating}",
            containerFactory = "bookRatingMessageKafkaListenerContainerFactory")
    public synchronized void listen(BookRatingMessage bookRatingMessage) {
        log.debug("On topic 'book-rating' received kafka message: {}", bookRatingMessage);
        bookService.updateBookRating(bookRatingMessage);
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-recommendation}",
            containerFactory = "bookRecommendationMessageKafkaListenerContainerFactory")
    public synchronized void listen(BookRecommendationMessage bookRecommendationMessage) {
        log.debug("On topic 'book-recommendation' received kafka message: {}", bookRecommendationMessage);
        bookService.updateBookRecommendations(bookRecommendationMessage);
    }
}
