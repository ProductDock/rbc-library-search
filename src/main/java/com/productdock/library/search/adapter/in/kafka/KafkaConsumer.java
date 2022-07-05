package com.productdock.library.search.adapter.in.kafka;

import com.productdock.library.search.adapter.in.kafka.messages.*;
import com.productdock.library.search.application.port.in.AddNewBookUseCase;
import com.productdock.library.search.application.service.BookService;
import com.productdock.library.search.domain.Book;
import com.productdock.library.search.domain.BookChanges;
import com.productdock.library.search.domain.BookField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public record KafkaConsumer(AddNewBookUseCase addNewBookUseCase, BookService bookService, InsertBookMessageMapper insertBookMessageMapper, RentalMessageMapper rentalMessageMapper) {

    @KafkaListener(topics = "${spring.kafka.topic.insert-book}",
            clientIdPrefix = "${spring.kafka.topic.insert-book}",
            containerFactory = "insertBookMessageKafkaListenerContainerFactory")
    public synchronized void listen(InsertBookMessage insertBookMessage) {
        log.debug("On topic 'insert-book' received kafka message: {}", insertBookMessage);
        var book = insertBookMessageMapper.toBook(insertBookMessage);
        addNewBookUseCase.addNewBook(book);
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-status}",
            clientIdPrefix = "${spring.kafka.topic.book-status}",
            containerFactory = "rentalMessageKafkaListenerContainerFactory")
    public synchronized void listen(RentalMessage rentalMessage) {
        log.debug("On topic 'book-status' received kafka message: {}", rentalMessage);
        var records = rentalMessageMapper.toRecords(rentalMessage.getRentalRecords());
        var updater = new BookChanges(BookField.RECORDS, records);
        bookService.updateBook(rentalMessage.getBookId(), updater);
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-availability}",
            clientIdPrefix = "${spring.kafka.topic.book-availability}",
            containerFactory = "bookAvailabilityMessageKafkaListenerContainerFactory")
    public synchronized void listen(BookAvailabilityMessage bookAvailabilityMessage) {
        log.debug("On topic 'book-availability' received kafka message: {}", bookAvailabilityMessage);
        var updater = new BookChanges(BookField.AVAILABLE_BOOK_COUNT, bookAvailabilityMessage.getAvailableBookCount());
        bookService.updateBook(bookAvailabilityMessage.getBookId(), updater);
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-rating}",
            clientIdPrefix = "${spring.kafka.topic.book-rating}",
            containerFactory = "bookRatingMessageKafkaListenerContainerFactory")
    public synchronized void listen(BookRatingMessage bookRatingMessage) {
        log.debug("On topic 'book-rating' received kafka message: {}", bookRatingMessage);
        var bookRating = new Book.Rating(bookRatingMessage.getRating(), bookRatingMessage.getRatingsCount());
        var updater = new BookChanges(BookField.RATING, bookRating);
        bookService.updateBook(bookRatingMessage.getBookId(), updater);
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-recommendation}",
            clientIdPrefix = "${spring.kafka.topic.book-recommendation}",
            containerFactory = "bookRecommendationMessageKafkaListenerContainerFactory")
    public synchronized void listen(BookRecommendationMessage bookRecommendationMessage) {
        log.debug("On topic 'book-recommendation' received kafka message: {}", bookRecommendationMessage);
        var updater = new BookChanges(BookField.RECOMMENDED, bookRecommendationMessage.getRecommended());
        bookService.updateBook(bookRecommendationMessage.getBookId(), updater);
    }
}
