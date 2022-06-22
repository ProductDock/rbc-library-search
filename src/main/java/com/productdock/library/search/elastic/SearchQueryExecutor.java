package com.productdock.library.search.elastic;

import com.productdock.library.search.elastic.document.BookDocument;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.data.domain.PageRequest;
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
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .withPageable(PageRequest.of(page, PAGE_SIZE))
                .build();

        return elasticsearchOperations.search(searchQuery, BookDocument.class);
    }

    public SearchHits<BookDocument> execute(BoolQueryBuilder query) {
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .build();

        return elasticsearchOperations.search(searchQuery, BookDocument.class);
    }
}
