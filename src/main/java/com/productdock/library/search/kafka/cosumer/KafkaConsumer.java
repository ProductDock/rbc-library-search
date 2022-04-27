package com.productdock.library.search.kafka.cosumer;

import com.productdock.library.search.book.BookMapper;
import com.productdock.library.search.book.BookService;
import com.productdock.library.search.kafka.cosumer.messages.InsertBookMessage;
import com.productdock.library.search.kafka.cosumer.messages.RentalMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public record KafkaConsumer(BookService bookService,
                            BookMapper bookMapper) {

    @KafkaListener(topics = "${spring.kafka.topic.insert-book-topic}",
            containerFactory = "insertBookMessageKafkaListenerContainerFactory")
    public synchronized void listen(InsertBookMessage insertBookMessage) {
        bookService.save(bookMapper.toBookDocument(insertBookMessage));
    }

    @KafkaListener(topics = "${spring.kafka.topic.book-status}",
            containerFactory = "rentalMessageKafkaListenerContainerFactory")
    public synchronized void listenForUpdate(RentalMessage rentalMessage) {
        bookService.update(rentalMessage);
    }
}
