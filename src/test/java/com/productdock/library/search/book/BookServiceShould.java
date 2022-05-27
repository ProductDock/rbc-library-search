package com.productdock.library.search.book;

import com.productdock.library.search.elastic.BookRatingMapper;
import com.productdock.library.search.elastic.RentalStateRecordMapper;
import com.productdock.library.search.elastic.RentalStateRecordMapperImpl;
import com.productdock.library.search.elastic.SearchQueryExecutor;
import com.productdock.library.search.elastic.document.BookDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsImpl;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.productdock.library.search.data.provider.BookRatingMessageMother.defaultBookRatingMessage;
import static com.productdock.library.search.data.provider.RentalMessageMother.defaultRentalMessage;
import static com.productdock.library.search.data.provider.BookAvailabilityMessageMother.defaultBookAvailabilityMessage;
import static com.productdock.library.search.data.provider.BookDocumentMother.defaultBookDocument;
import static com.productdock.library.search.data.provider.BookRecommendationMessageMother.defaultBookRecommendationMessageBuilder;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {RentalStateRecordMapperImpl.class})
class BookServiceShould {

    private static final Optional<List<String>> ANY_TOPIC = Optional.of(List.of("TOPIC"));
    private static final boolean RECOMMENDED = false;
    private static final List MAPPED_DOCUMENT_RECORDS = Collections.singletonList
            (new BookDocument.RentalState.Record("any", BookStatus.RENTED));
    private static final BookDocument.Rating MAPPED_BOOK_RATING = mock(BookDocument.Rating.class);


    @InjectMocks
    private BookService bookService;

    @Mock
    private SearchQueryExecutor searchQueryExecutor;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private RentalStateRecordMapper rentalStateRecordMapper;

    @Mock
    private BookRatingMapper bookRatingMapper;

    @Mock
    private BookDocumentRepository bookDocumentRepository;

    @Test
    void getBooksByTopics() {
        var firstPage = 0;
        var searchFilters = new SearchFilters(firstPage, RECOMMENDED, ANY_TOPIC);

        given(searchQueryExecutor.execute(searchFilters)).willReturn(aBookSearchHits());

        var books = bookService.getBooks(searchFilters);

        assertThat(books.count).isEqualTo(2);
        assertThat(books.books).hasSize(2);
    }

    private SearchHits<BookDocument> aBookSearchHits() {
        List bookIndices = of(
                mock(SearchHit.class),
                mock(SearchHit.class)
        ).collect(toList());

        return new SearchHitsImpl<>(bookIndices.size(),
                null,
                0,
                null,
                bookIndices,
                null,
                null);
    }

    @Test
    void updateBookRecords() {
        var rentalMessage = defaultRentalMessage();
        var bookDocument = defaultBookDocument();

        given(bookDocumentRepository.findById(rentalMessage.getBookId())).willReturn(Optional.ofNullable(bookDocument));
        given(rentalStateRecordMapper.toRecords(rentalMessage.getRentalRecords())).willReturn(MAPPED_DOCUMENT_RECORDS);

        bookService.updateBookRecords(rentalMessage);

        assertThat(bookDocument.getRentalState().getRecords()).isEqualTo(MAPPED_DOCUMENT_RECORDS);
    }

    @Test
    void updateBookRating() {
        var bookRatingMessage = defaultBookRatingMessage();
        var bookDocument = defaultBookDocument();

        given(bookDocumentRepository.findById(bookRatingMessage.getBookId())).willReturn(Optional.ofNullable(bookDocument));
        given(bookRatingMapper.toRating(bookRatingMessage)).willReturn(MAPPED_BOOK_RATING);

        bookService.updateBookRating(bookRatingMessage);

        assertThat(bookDocument.getRating()).isEqualTo(MAPPED_BOOK_RATING);
    }

    @Test
    void updateAvailabilityBookCount() {
        var bookAvailabilityMessage = defaultBookAvailabilityMessage();
        var bookDocument = defaultBookDocument();

        given(bookDocumentRepository.findById(bookAvailabilityMessage.getBookId())).willReturn(Optional.ofNullable(bookDocument));

        bookService.updateAvailabilityBookCount(bookAvailabilityMessage);

        verify(bookDocumentRepository).save(bookDocument);
        assertThat(bookDocument.getRentalState().getAvailableBooksCount())
                .isEqualTo(bookAvailabilityMessage.getAvailableBookCount());
    }

    @Test
    void updateBookRecommendations() {
        var bookRecommendationMessage = defaultBookRecommendationMessageBuilder().recommended(true).build();
        var bookDocument = defaultBookDocument();

        given(bookDocumentRepository.findById(bookRecommendationMessage.getBookId())).willReturn(Optional.ofNullable(bookDocument));

        bookService.updateBookRecommendations(bookRecommendationMessage);

        verify(bookDocumentRepository).save(bookDocument);
        assertThat(bookDocument.isRecommended()).isTrue();
    }
}
