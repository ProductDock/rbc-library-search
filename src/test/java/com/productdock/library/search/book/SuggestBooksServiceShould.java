package com.productdock.library.search.book;

import com.productdock.library.search.clean.BookSearchSuggestionDtoMapper;
import com.productdock.library.search.clean.SearchFilters;
import com.productdock.library.search.clean.SuggestBooksService;
import com.productdock.library.search.elastic.SearchQueryBuilder;
import com.productdock.library.search.elastic.SearchQueryExecutor;
import com.productdock.library.search.elastic.document.BookDocument;
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
class SuggestBooksServiceShould {

    private static final Optional<List<String>> ANY_TOPIC = Optional.of(List.of("TOPIC"));
    private static final boolean RECOMMENDED = false;
    private static final Optional<String> SEARCH_TEXT = Optional.of("");
    private static final int FIRST_PAGE = 0;

    @InjectMocks
    private SuggestBooksService suggestBooksService;

    @Mock
    private SearchQueryExecutor searchQueryExecutor;

    @Mock
    private SearchQueryBuilder searchQueryBuilder;

    @Mock
    private BookSearchSuggestionDtoMapper bookSearchSuggestionDtoMapper;

    @Test
    void getBookSuggestions() {
        var searchFilters = SearchFilters.builder().searchText(SEARCH_TEXT).build();
        var buildQuery = mock(BoolQueryBuilder.class);
        given(searchQueryBuilder.buildWith(searchFilters)).willReturn(buildQuery);
        given(searchQueryExecutor.execute(buildQuery)).willReturn(aBookSearchHits());

        var books = suggestBooksService.searchBookSuggestions(searchFilters);

        assertThat(books).hasSize(2);
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
