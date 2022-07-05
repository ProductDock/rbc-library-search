package com.productdock.library.search.adapter.in.web;

import com.productdock.library.search.adapter.in.web.mapper.BookRecordMapperImpl;
import com.productdock.library.search.adapter.in.web.mapper.SearchBooksMapper;
import com.productdock.library.search.domain.Book;
import com.productdock.library.search.domain.BookStatus;
import com.productdock.library.search.domain.SearchBooksResultsPage;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.productdock.library.search.data.provider.BookMother.defaultBook;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SearchBooksMapper.class, BookRecordMapperImpl.class})
class SearchBooksMapperShould {

    private static final int BOOK_COUNT = 2;
    private static final List<Book> BOOKS = List.of(defaultBook(), defaultBook());

    @Autowired
    private SearchBooksMapper searchBooksMapper;

    @Test
    void mapSearchBooksResultToSearchBooksResponse() {
        var searchBookResult = SearchBooksResultsPage.builder().count(BOOK_COUNT).books(BOOKS).build();

        var searchBookResponse = searchBooksMapper.toResponse(searchBookResult);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(searchBookResponse.count).isEqualTo(searchBookResult.count);
            softly.assertThat(searchBookResponse.books).hasSize(2);
        }
    }

    @Test
    void mapBookDocumentToBookDto_whenRatingNotPresent() {
        var book = Book.builder()
                .rentalState(mock(Book.RentalState.class))
                .rating(null)
                .build();
        var searchBookResult = SearchBooksResultsPage.builder().count(BOOK_COUNT).books(List.of(book)).build();

        var searchBookResponse = searchBooksMapper.toResponse(searchBookResult);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(searchBookResponse.books.get(0).rating.count).isNull();
            softly.assertThat(searchBookResponse.books.get(0).rating.score).isNull();
        }
    }

    @Test
    void mapBookDocumentToBookDto_whenBookStatusWrapperHasOneRecordAndOneAvailableBookCount() {
        var book =
                Book.builder()
                        .id("123")
                        .cover("Book cover")
                        .rentalState(Book.RentalState.builder()
                                .availableBooksCount(1)
                                .records(List.of(Book.RentalState.Record.builder().email("email").status(BookStatus.RENTED).build()))
                                .build()).build();
        var searchBookResult = SearchBooksResultsPage.builder().count(BOOK_COUNT).books(List.of(book)).build();

        var searchBookResponse = searchBooksMapper.toResponse(searchBookResult);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(searchBookResponse.books.get(0).records)
                    .extracting("email", "status")
                    .containsExactlyInAnyOrder(
                            tuple(null, BookStatus.AVAILABLE),
                            tuple("email", BookStatus.RENTED)
                    );
        }
    }
}
