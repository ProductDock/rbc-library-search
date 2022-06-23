package com.productdock.library.search.elastic;

import com.productdock.library.search.book.SearchFilters;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.stereotype.Service;

import static com.productdock.library.search.elastic.BookQueryBuilder.bookQueryBuilder;
@Service
@AllArgsConstructor
@Slf4j
public class SearchQueryBuilder {

    public BoolQueryBuilder buildWith(SearchFilters searchFilters) {
        log.debug("Build search query with search filters: {}", searchFilters);
        return bookQueryBuilder()
                .withTopicsCriteria(searchFilters.getTopics())
                .andRecommendation(searchFilters.isRecommended())
                .andSearchByTitleAndAuthor(searchFilters.getSearchText())
                .build();
    }

}
