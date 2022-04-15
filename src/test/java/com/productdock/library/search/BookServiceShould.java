package com.productdock.library.search;

import com.productdock.library.search.elastic.document.BookDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsImpl;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class BookServiceShould {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookDocumentRepository bookDocumentRepository;

    @Mock
    private SearchQueryExecutor searchQueryExecutor;

    @Mock
    private BookMapper bookMapper;

    @Test
    void getBooksByTopics() {
        var topicsFilter = Optional.of(List.of("TOPIC"));
        var firstPage = 0;

        given(searchQueryExecutor.execute(topicsFilter, firstPage)).willReturn(aBookSearchHits());

        var books = bookService.getBooks(topicsFilter, 0);

        assertThat(books.count).isEqualTo(2);
        assertThat(books.books).hasSize(2);
    }

    private SearchHits<BookDocument> aBookSearchHits() {
        SearchHit<BookDocument> firstHit = mock(SearchHit.class);
        SearchHit<BookDocument> secondHit = mock(SearchHit.class);

        List<SearchHit<BookDocument>> bookIndices = of(
                firstHit,
                secondHit
        ).collect(toList());

        return new SearchHitsImpl<>(bookIndices.size(),
                null,
                0,
                null,
                bookIndices,
                null,
                null);
    }
}
