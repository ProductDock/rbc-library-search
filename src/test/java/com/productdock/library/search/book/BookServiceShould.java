package com.productdock.library.search.book;

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

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class BookServiceShould {

    private static final Optional<List<String>> ANY_TOPIC = Optional.of(List.of("TOPIC"));

    @InjectMocks
    private BookService bookService;

    @Mock
    private SearchQueryExecutor searchQueryExecutor;

    @Mock
    private BookMapper bookMapper;

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
}
