package com.productdock.library.search.book;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.productdock.library.search.data.provider.RentalMessageMother.defaultRentalMessage;
import static com.productdock.library.search.data.provider.BookAvailabilityMessageMother.defaultBookAvailabilityMessage;
import static com.productdock.library.search.data.provider.BookDocumentMother.defaultBookDocument;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {RentalStateRecordMapperImpl.class})
class BookServiceShould {

    private static final Optional<List<String>> ANY_TOPIC = Optional.of(List.of("TOPIC"));

    @InjectMocks
    private BookService bookService;

    @Mock
    private SearchQueryExecutor searchQueryExecutor;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private RentalStateRecordMapper rentalStateRecordMapper;

    @Mock
    private BookDocumentRepository bookDocumentRepository;

    @Test
    void getBooksByTopics() {
        var firstPage = 0;

        given(searchQueryExecutor.execute(ANY_TOPIC, firstPage)).willReturn(aBookSearchHits());

        var books = bookService.getBooks(ANY_TOPIC, 0);

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
        given(rentalStateRecordMapper.toRecords(rentalMessage.getRecords())).willReturn(new ArrayList<>());

        bookService.updateBookRecords(rentalMessage);

        verify(bookDocumentRepository).save(bookDocument);
        assertThat(bookDocument.getRentalState().getRecords()).isEmpty();
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
}
