package com.productdock.library.search.adapter.in.kafka;

import com.productdock.library.search.adapter.in.kafka.messages.*;
import com.productdock.library.search.application.port.in.AddNewBookUseCase;
import com.productdock.library.search.application.service.BookService;
import com.productdock.library.search.domain.Book;
import com.productdock.library.search.domain.BookChanges;
import com.productdock.library.search.domain.BookField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public record KafkaConsumer(AddNewBookUseCase addNewBookUseCase, BookService bookService, InsertBookMessageMapper insertBookMessageMapper, RentalMessageMapper rentalMessageMapper) {

    @KafkaListener(topics = "${spring.kafka.topic.insert-book}",
            clientIdPrefix = "${spring.kafka.topic.insert-book}",
            containerFactory = "insertBookMessageKafkaListenerContainerFactory")
    public synchronized void listen(InsertBookMessage insertBookMessage, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.debug("On topic {} received kafka message: {}", topic, insertBookMessage);
        var book = insertBookMessageMapper.toBook(insertBookMessage);
        addNewBookUseCase.addNewBook(book);
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-status}",
            clientIdPrefix = "${spring.kafka.topic.book-status}",
            containerFactory = "rentalMessageKafkaListenerContainerFactory")
    public synchronized void listen(RentalMessage rentalMessage, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.debug("On topic {} received kafka message: {}", topic, rentalMessage);
        var records = rentalMessageMapper.toRecords(rentalMessage.getRentalRecords());
        var changes = new BookChanges(BookField.RECORDS, records);
        bookService.updateBook(rentalMessage.getBookId(), changes);
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-availability}",
            clientIdPrefix = "${spring.kafka.topic.book-availability}",
            containerFactory = "bookAvailabilityMessageKafkaListenerContainerFactory")
    public synchronized void listen(BookAvailabilityMessage bookAvailabilityMessage, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.debug("On topic {} received kafka message: {}", topic, bookAvailabilityMessage);
        var changes = new BookChanges(BookField.AVAILABLE_BOOK_COUNT, bookAvailabilityMessage.getAvailableBookCount());
        bookService.updateBook(bookAvailabilityMessage.getBookId(), changes);
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-rating}",
            clientIdPrefix = "${spring.kafka.topic.book-rating}",
            containerFactory = "bookRatingMessageKafkaListenerContainerFactory")
    public synchronized void listen(BookRatingMessage bookRatingMessage, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.debug("On topic {} received kafka message: {}", topic, bookRatingMessage);
        var bookRating = new Book.Rating(bookRatingMessage.getRating(), bookRatingMessage.getRatingsCount());
        var changes = new BookChanges(BookField.RATING, bookRating);
        bookService.updateBook(bookRatingMessage.getBookId(), changes);
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-recommendation}",
            clientIdPrefix = "${spring.kafka.topic.book-recommendation}",
            containerFactory = "bookRecommendationMessageKafkaListenerContainerFactory")
    public synchronized void listen(BookRecommendationMessage bookRecommendationMessage, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.debug("On topic {} received kafka message: {}", topic, bookRecommendationMessage);
        var changes = new BookChanges(BookField.RECOMMENDED, bookRecommendationMessage.getRecommended());
        bookService.updateBook(bookRecommendationMessage.getBookId(), changes);
    }
}
