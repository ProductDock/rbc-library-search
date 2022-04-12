package com.productdock.library.search;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public record SearchQueryExecutor(ElasticsearchOperations elasticsearchOperations) {

    private static final int PAGE_SIZE = 2;

    public SearchHits<BookIndex> execute(Optional<List<String>> topicsFilter, int page) {
        QueryBuilderDecorator queryBuilder = new QueryBuilderDecorator();
        topicsFilter.ifPresent(list -> queryBuilder.addTopicsCriteria(list));
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder.getBuilder())
                .withPageable(PageRequest.of(page, PAGE_SIZE))
                .build();

        return elasticsearchOperations.search(searchQuery, BookIndex.class);
    }
}
