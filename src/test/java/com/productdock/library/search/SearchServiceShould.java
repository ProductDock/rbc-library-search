package com.productdock.library.search;

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
public class SearchServiceShould {

    @InjectMocks
    private SearchService searchService;

    @Mock
    private BookIndexRepository bookIndexRepository;

    @Mock
    private SearchQueryExecutor searchQueryExecutor;

    @Mock
    private BookMapper bookMapper;

    @Test
    void getBooksByTopics() {
        var topicsFilter = Optional.of(List.of("TOPIC"));
        var firstPage = 0;

        given(searchQueryExecutor.execute(topicsFilter, firstPage)).willReturn(aBookSearchHits());

        var books = searchService.getBooks(topicsFilter, 0);

        assertThat(books.count).isEqualTo(2);
        assertThat(books.books).hasSize(2);
    }

    private SearchHits<BookIndex> aBookSearchHits() {
        SearchHit<BookIndex> firstHit = mock(SearchHit.class);
        SearchHit<BookIndex> secondHit = mock(SearchHit.class);

        List<SearchHit<BookIndex>> bookIndices = of(
                firstHit,
                secondHit
        ).collect(toList());

        return new SearchHitsImpl<>(bookIndices.size(), null, 0, null, bookIndices, null, null);
    }
}
