package com.productdock.library.search.clean;

import com.productdock.library.search.elastic.SearchQueryBuilder;
import com.productdock.library.search.elastic.SearchQueryExecutor;
import com.productdock.library.search.elastic.document.BookDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public record SuggestBooksService(SearchQueryBuilder searchQueryBuilder, SearchQueryExecutor searchQueryExecutor,
                                  BookSearchSuggestionDtoMapper bookSearchSuggestionDtoMapper) implements SuggestBooksQuery{

    @Override
    public List<BookDocument> searchBookSuggestions(SearchFilters searchFilters) {
        log.debug("Get book suggestions by search text: {}", searchFilters.getSearchText());
        var query = searchQueryBuilder.buildWith(searchFilters);
        var hits = searchQueryExecutor.execute(query);
        return hits.stream().map(SearchHit::getContent).toList();
    }
}
