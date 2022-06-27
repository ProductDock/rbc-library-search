package com.productdock.library.search.clean;

import com.productdock.library.search.elastic.SearchQueryBuilder;
import com.productdock.library.search.elastic.SearchQueryExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public record FindBooksService(SearchQueryBuilder searchQueryBuilder, SearchQueryExecutor searchQueryExecutor,
                               BookMapper bookMapper) implements FindBooksQuery {
    @Override
    public SearchBooksResult getBooks(SearchFilters searchFilters, int page) {
        log.debug("Get books by search filters: {}", searchFilters);
        var query = searchQueryBuilder.buildWith(searchFilters);
        var hits = searchQueryExecutor.execute(query, page);
        var books = hits.stream().map(hit -> bookMapper.toBook(hit.getContent())).toList();
        return new SearchBooksResult(hits.getTotalHits(), books);
    }
}
