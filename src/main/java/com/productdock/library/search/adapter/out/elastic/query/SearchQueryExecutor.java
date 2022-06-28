package com.productdock.library.search.adapter.out.elastic.query;

import com.productdock.library.search.adapter.out.elastic.BookDocument;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SearchQueryExecutor {

    private static final int PAGE_SIZE = 18;
    private ElasticsearchOperations elasticsearchOperations;

    public SearchHits<BookDocument> execute(BoolQueryBuilder query, int page) {
        return executeWithPageable(query, PageRequest.of(page, PAGE_SIZE));
    }

    private SearchHits<BookDocument> executeWithPageable(BoolQueryBuilder query, Pageable pageable){
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .withPageable(pageable)
                .build();

        return elasticsearchOperations.search(searchQuery, BookDocument.class);
    }

    public SearchHits<BookDocument> execute(BoolQueryBuilder query) {
        return executeWithPageable(query, Pageable.unpaged());
    }
}
