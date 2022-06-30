package com.productdock.library.search.application.service;

import com.productdock.library.search.application.port.out.persistence.BookDocumentPersistenceOutPort;
import com.productdock.library.search.domain.Book;
import com.productdock.library.search.domain.BookChanges;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceShould {

    private static final String BOOK_ID = "1";

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookDocumentPersistenceOutPort bookDocumentRepository;

    @Test
    void updateBook() {
        var book = mock(Book.class);
        var updater = mock(BookChanges.class);
        given(bookDocumentRepository.findById(BOOK_ID)).willReturn(Optional.ofNullable(book));

        bookService.updateBook(BOOK_ID, updater);

        verify(book).update(updater);
        verify(bookDocumentRepository).save(book);
    }

}
