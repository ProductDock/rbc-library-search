package com.productdock.library.search.cosumer;

import com.productdock.library.search.BookMapper;
import com.productdock.library.search.BookService;
import com.productdock.library.search.cosumer.messages.InsertBook;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public record KafkaConsumer(BookService bookService,
                            BookMapper bookMapper) {

    @KafkaListener(topics = "${spring.kafka.topic.insert-book-topic}",
            containerFactory = "insertBookMessageKafkaListenerContainerFactory")
    public synchronized void listen(InsertBook insertBookMessage) {
        bookService.save(bookMapper.toBookIndex(insertBookMessage));
    }
}
