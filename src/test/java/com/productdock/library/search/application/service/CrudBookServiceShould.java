package com.productdock.library.search.application.service;

import com.productdock.library.search.application.port.out.persistence.BookPersistenceOutPort;
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
class CrudBookServiceShould {

    private static final String BOOK_ID = "1";

    @InjectMocks
    private CrudBookService crudBookService;

    @Mock
    private BookPersistenceOutPort bookRepository;

    @Test
    void updateBook() {
        var book = mock(Book.class);
        var changes = mock(BookChanges.class);
        given(bookRepository.findById(BOOK_ID)).willReturn(Optional.ofNullable(book));

        crudBookService.updateBook(BOOK_ID, changes);

        verify(book).update(changes);
        verify(bookRepository).save(book);
    }

    @Test
    void addNewBook() {
        var book = mock(Book.class);

        crudBookService.addNewBook(book);

        verify(bookRepository).save(book);
    }

}
