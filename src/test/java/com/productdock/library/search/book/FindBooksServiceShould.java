package com.productdock.library.search.book;

import com.productdock.library.search.application.service.SearchBooksService;
import com.productdock.library.search.domain.SearchFilters;
import com.productdock.library.search.adapter.out.elastic.query.SearchQueryBuilder;
import com.productdock.library.search.adapter.out.elastic.query.SearchQueryExecutor;
import com.productdock.library.search.adapter.out.elastic.BookDocument;
import org.elasticsearch.index.query.BoolQueryBuilder;
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
class FindBooksServiceShould {

    private static final Optional<List<String>> ANY_TOPIC = Optional.of(List.of("TOPIC"));
    private static final boolean RECOMMENDED = false;
    private static final Optional<String> SEARCH_TEXT = Optional.of("");
    private static final int FIRST_PAGE = 0;

    @InjectMocks
    private SearchBooksService findBooksService;

    @Mock
    private SearchQueryExecutor searchQueryExecutor;

    @Mock
    private SearchQueryBuilder searchQueryBuilder;

    @Test
    void getBooksByTopics() {
        var searchFilters = SearchFilters.builder().recommended(RECOMMENDED).topics(ANY_TOPIC).searchText(SEARCH_TEXT).build();
        var buildQuery = mock(BoolQueryBuilder.class);
        given(searchQueryBuilder.buildWith(searchFilters)).willReturn(buildQuery);
        given(searchQueryExecutor.execute(buildQuery, FIRST_PAGE)).willReturn(aBookSearchHits());

        var searchBooksResult = findBooksService.searchBooks(searchFilters, FIRST_PAGE);

        assertThat(searchBooksResult.getBooks()).hasSize(2);
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
