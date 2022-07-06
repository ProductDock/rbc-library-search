package com.productdock.library.search.adapter.in.kafka;

import com.productdock.library.search.adapter.in.kafka.mappers.InsertBookMessageMapper;
import com.productdock.library.search.adapter.in.kafka.mappers.RentalMessageMapper;
import com.productdock.library.search.adapter.in.kafka.messages.*;
import com.productdock.library.search.application.port.in.AddNewBookUseCase;
import com.productdock.library.search.application.port.in.UpdateBookUseCase;
import com.productdock.library.search.domain.Book;
import com.productdock.library.search.domain.BookChanges;
import com.productdock.library.search.domain.BookField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.RECEIVED_TOPIC;

@Service
@Slf4j
public record KafkaConsumer(AddNewBookUseCase addNewBookUseCase, UpdateBookUseCase updateBookUseCase, InsertBookMessageMapper insertBookMessageMapper, RentalMessageMapper rentalMessageMapper) {

    private static final String LOG_TEMPLATE = "On topic {} received kafka message: {}";

    @KafkaListener(topics = "${spring.kafka.topic.insert-book}",
            clientIdPrefix = "${spring.kafka.topic.insert-book}",
            containerFactory = "insertBookMessageKafkaListenerContainerFactory")
    public synchronized void listen(InsertBookMessage message, @Header(RECEIVED_TOPIC) String topic) {
        log.debug(LOG_TEMPLATE, topic, message);
        var book = insertBookMessageMapper.toBook(message);
        addNewBookUseCase.addNewBook(book);
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-status}",
            clientIdPrefix = "${spring.kafka.topic.book-status}",
            containerFactory = "rentalMessageKafkaListenerContainerFactory")
    public synchronized void listen(RentalMessage message, @Header(RECEIVED_TOPIC) String topic) {
        log.debug(LOG_TEMPLATE, topic, message);
        var records = rentalMessageMapper.toRecords(message.getRentalRecords());
        var changes = new BookChanges(BookField.RENTAL_RECORDS, records);
        updateBookUseCase.updateBook(message.getBookId(), changes);
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-availability}",
            clientIdPrefix = "${spring.kafka.topic.book-availability}",
            containerFactory = "bookAvailabilityMessageKafkaListenerContainerFactory")
    public synchronized void listen(BookAvailabilityMessage message, @Header(RECEIVED_TOPIC) String topic) {
        log.debug(LOG_TEMPLATE, topic, message);
        var changes = new BookChanges(BookField.AVAILABLE_BOOK_COUNT, message.getAvailableBookCount());
        updateBookUseCase.updateBook(message.getBookId(), changes);
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-rating}",
            clientIdPrefix = "${spring.kafka.topic.book-rating}",
            containerFactory = "bookRatingMessageKafkaListenerContainerFactory")
    public synchronized void listen(BookRatingMessage message, @Header(RECEIVED_TOPIC) String topic) {
        log.debug(LOG_TEMPLATE, topic, message);
        var bookRating = new Book.Rating(message.getRating(), message.getRatingsCount());
        var changes = new BookChanges(BookField.RATING, bookRating);
        updateBookUseCase.updateBook(message.getBookId(), changes);
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-recommendation}",
            clientIdPrefix = "${spring.kafka.topic.book-recommendation}",
            containerFactory = "bookRecommendationMessageKafkaListenerContainerFactory")
    public synchronized void listen(BookRecommendationMessage message, @Header(RECEIVED_TOPIC) String topic) {
        log.debug(LOG_TEMPLATE, topic, message);
        var changes = new BookChanges(BookField.RECOMMENDED, message.getRecommended());
        updateBookUseCase.updateBook(message.getBookId(), changes);
    }
}
