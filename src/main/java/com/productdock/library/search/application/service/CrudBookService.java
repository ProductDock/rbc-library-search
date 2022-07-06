package com.productdock.library.search.application.service;

import com.productdock.library.search.application.port.in.AddNewBookUseCase;
import com.productdock.library.search.application.port.in.UpdateBookUseCase;
import com.productdock.library.search.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.library.search.domain.Book;
import com.productdock.library.search.domain.BookChanges;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public record CrudBookService(BookPersistenceOutPort bookPersistenceOutPort) implements AddNewBookUseCase, UpdateBookUseCase {

    @Override
    public void updateBook(String bookId, BookChanges changes) {
        log.debug("Update book with id: {}", bookId);
        var book = bookPersistenceOutPort.findById(bookId).orElseThrow();
        book.update(changes);
        bookPersistenceOutPort.save(book);
    }

    @Override
    public void addNewBook(Book book) {
        log.debug("Add new book: {}", book);
        bookPersistenceOutPort.save(book);
    }
}
