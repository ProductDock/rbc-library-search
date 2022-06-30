package com.productdock.library.search.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.productdock.library.search.data.provider.BookMother.defaultBook;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BookShould {

    private static final Book BOOK = defaultBook();

    @Test
    void updateRentalStateRecords(){
        var newRecords = List.of(Book.RentalState.Record.class, Book.RentalState.Record.class);
        var bookChanges = new BookChanges(BookField.RECORDS, newRecords);

        BOOK.update(bookChanges);

        assertThat(BOOK.getRentalState().getRecords()).hasSize(2);
    }

    @Test
    void updateAvailableBookCount(){
        var availableBookCount = 1;
        var bookChanges = new BookChanges(BookField.AVAILABLE_BOOK_COUNT, availableBookCount);

        BOOK.update(bookChanges);

        assertThat(BOOK.getRentalState().getAvailableBooksCount()).isEqualTo(1);
    }

    @Test
    void updateRating(){
        var newRating = new Book.Rating(5.0,1);
        var bookChanges = new BookChanges(BookField.RATING, newRating);

        BOOK.update(bookChanges);

        assertThat(BOOK.getRating().getScore()).isEqualTo(5.0);
        assertThat(BOOK.getRating().getCount()).isEqualTo(1);
    }

    @Test
    void updateBookToRecommended(){
        var bookChanges = new BookChanges(BookField.RECOMMENDED, true);

        BOOK.update(bookChanges);

        assertThat(BOOK.isRecommended()).isTrue();
    }
}
