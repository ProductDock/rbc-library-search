package com.productdock.library.search.book;

import com.productdock.library.search.elastic.SearchQueryBuilder;
import com.productdock.library.search.elastic.SearchQueryExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public record BookSearchService(SearchQueryBuilder searchQueryBuilder, SearchQueryExecutor searchQueryExecutor,
                                BookSearchSuggestionDtoMapper bookSearchSuggestionDtoMapper, BookMapper bookMapper) {

    public SearchBooksResponse getBooks(SearchFilters searchFilters, int page) {
        log.debug("Get books by search filters: {}", searchFilters);
        var query = searchQueryBuilder.buildWith(searchFilters);
        var hits = searchQueryExecutor.execute(query, page);
        var bookHitsDto = hits.stream().map(hit -> bookMapper.toBookDto(hit.getContent())).toList();
        return new SearchBooksResponse(hits.getTotalHits(), bookHitsDto);
    }

    public List<BookSearchSuggestionDto> searchBookSuggestions(SearchFilters searchFilters) {
        log.debug("Get book suggestions by search text: {}", searchFilters.getSearchText());
        var query = searchQueryBuilder.buildWith(searchFilters);
        var hits = searchQueryExecutor.execute(query);
        return hits.stream().map(hit -> bookSearchSuggestionDtoMapper.toBookSearchSuggestionDto(hit.getContent())).toList();
    }
}
