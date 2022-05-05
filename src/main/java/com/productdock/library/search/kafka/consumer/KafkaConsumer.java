package com.productdock.library.search.kafka.consumer;

import com.productdock.library.search.book.BookMapper;
import com.productdock.library.search.book.BookService;
import com.productdock.library.search.kafka.consumer.messages.BookAvailabilityMessage;
import com.productdock.library.search.kafka.consumer.messages.InsertBookMessage;
import com.productdock.library.search.kafka.consumer.messages.RentalMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public record KafkaConsumer(BookService bookService,
                            BookMapper bookMapper) {

    @KafkaListener(topics = "${spring.kafka.topic.insert-book}",
            containerFactory = "insertBookMessageKafkaListenerContainerFactory")
    public synchronized void listen(InsertBookMessage insertBookMessage) {
        bookService.save(bookMapper.toBookDocument(insertBookMessage));
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-status}",
            containerFactory = "rentalMessageKafkaListenerContainerFactory")
    public synchronized void listen(RentalMessage rentalMessage) {
        bookService.updateBookRecords(rentalMessage);
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-availability}",
            containerFactory = "bookAvailabilityMessageKafkaListenerContainerFactory")
    public synchronized void listen(BookAvailabilityMessage bookAvailabilityMessage) {
        bookService.updateAvailabilityBookCount(bookAvailabilityMessage);
    }
}
