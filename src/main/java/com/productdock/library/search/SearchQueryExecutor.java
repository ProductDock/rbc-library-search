package com.productdock.library.search;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record SearchQueryExecutor(ElasticsearchOperations elasticsearchOperations) {

    private static final int PAGE_SIZE = 2;

    public  SearchHits<BookIndex> execute(List<String> topicsFilter, int page) {
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        topicsFilter.forEach(topic -> queryBuilder.should(QueryBuilders.matchQuery(SearchFields.TOPICS_NAME, topic)));
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(page, PAGE_SIZE))
                .build();

        return elasticsearchOperations.search(searchQuery, BookIndex.class);
    }

}
