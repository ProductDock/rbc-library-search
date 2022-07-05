package com.productdock.library.search.application.service;

import com.productdock.library.search.application.port.in.AddNewBookUseCase;
import com.productdock.library.search.application.port.out.persistence.BookDocumentPersistenceOutPort;
import com.productdock.library.search.domain.Book;
import com.productdock.library.search.domain.BookChanges;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public record BookService(BookDocumentPersistenceOutPort bookDocumentPersistenceOutPort) implements AddNewBookUseCase {

    public void updateBook(String bookId, BookChanges updater) {
        log.debug("Update book with id: {}", bookId);
        var book = getBook(bookId);
        book.update(updater);
        bookDocumentPersistenceOutPort.save(book);
    }

    private Book getBook(String bookId) {
        log.debug("Find book with id: {}", bookId);
        return bookDocumentPersistenceOutPort.findById(bookId).orElseThrow();
    }

    @Override
    public void addNewBook(Book book) {
        bookDocumentPersistenceOutPort.save(book);
    }
}
