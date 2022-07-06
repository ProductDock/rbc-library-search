package com.productdock.library.search.application.service;

import com.productdock.library.search.application.port.in.AddNewBookUseCase;
import com.productdock.library.search.application.port.in.UpdateBookUseCase;
import com.productdock.library.search.application.port.out.persistence.BookDocumentPersistenceOutPort;
import com.productdock.library.search.domain.Book;
import com.productdock.library.search.domain.BookChanges;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public record BookService(BookDocumentPersistenceOutPort bookDocumentPersistenceOutPort) implements AddNewBookUseCase, UpdateBookUseCase {

    public void updateBook(String bookId, BookChanges changes) {
        log.debug("Update book with id: {}", bookId);
        var book = bookDocumentPersistenceOutPort.findById(bookId).orElseThrow();
        book.update(changes);
        bookDocumentPersistenceOutPort.save(book);
    }

    @Override
    public void addNewBook(Book book) {
        bookDocumentPersistenceOutPort.save(book);
    }
}
