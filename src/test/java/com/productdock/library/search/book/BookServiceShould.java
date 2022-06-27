package com.productdock.library.search.book;

import com.productdock.library.search.clean.BookStatus;
import com.productdock.library.search.elastic.BookRatingMapper;
import com.productdock.library.search.elastic.RentalStateRecordMapper;
import com.productdock.library.search.elastic.document.BookDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.productdock.library.search.data.provider.BookRatingMessageMother.defaultBookRatingMessage;
import static com.productdock.library.search.data.provider.RentalMessageMother.defaultRentalMessage;
import static com.productdock.library.search.data.provider.BookAvailabilityMessageMother.defaultBookAvailabilityMessage;
import static com.productdock.library.search.data.provider.BookDocumentMother.defaultBookDocument;
import static com.productdock.library.search.data.provider.BookRecommendationMessageMother.defaultBookRecommendationMessageBuilder;

import static java.util.stream.Stream.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceShould {

    private static final List MAPPED_DOCUMENT_RECORDS = Collections.singletonList
            (new BookDocument.RentalState.Record("any", BookStatus.RENTED));
    private static final BookDocument.Rating MAPPED_BOOK_RATING = mock(BookDocument.Rating.class);


    @InjectMocks
    private BookService bookService;

    @Mock
    private RentalStateRecordMapper rentalStateRecordMapper;

    @Mock
    private BookRatingMapper bookRatingMapper;

    @Mock
    private BookDocumentRepository bookDocumentRepository;

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
