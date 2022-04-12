package com.productdock.library.search;

import org.elasticsearch.index.query.BoolQueryBuilder;
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

    private static final int PAGE_SIZE = 18;

    public SearchHits<BookIndex> execute(Optional<List<String>> topicsFilter, int page) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        QueryBuilderDecorator enrichedBuilder = new QueryBuilderDecorator(boolQueryBuilder);
        topicsFilter.ifPresent(list -> enrichedBuilder.addTopicsCriteria(list));
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(PageRequest.of(page, PAGE_SIZE))
                .build();

        return elasticsearchOperations.search(searchQuery, BookIndex.class);
    }
}
