package com.productdock.library.search.elastic;

import com.productdock.library.search.book.SearchFilters;
import com.productdock.library.search.elastic.document.BookDocument;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import static com.productdock.library.search.elastic.BookQueryBuilder.bookQueryBuilder;

@Service
@AllArgsConstructor
@Slf4j
public class SearchQueryExecutor {

    private static final int PAGE_SIZE = 18;
    private ElasticsearchOperations elasticsearchOperations;

    public SearchHits<BookDocument> execute(SearchFilters searchFilters) {
        log.debug("Execute search query with search filters: {}", searchFilters);
        var queryBuilder = bookQueryBuilder()
                .withTopicsCriteria(searchFilters.getTopics())
                .andRecommendation(searchFilters.isRecommended())
                .andSearchByTitleAndAuthor(searchFilters.getSearchText())
                .build();
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(doPagination(searchFilters.getPage()))
                .build();

        return elasticsearchOperations.search(searchQuery, BookDocument.class);
    }

    private Pageable doPagination(Integer page) {
        return page == null ? Pageable.unpaged() : PageRequest.of(page, PAGE_SIZE);
    }
}
