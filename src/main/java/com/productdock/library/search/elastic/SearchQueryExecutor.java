package com.productdock.library.search.elastic;

import com.productdock.library.search.elastic.document.BookDocument;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.productdock.library.search.elastic.BookQueryBuilder.bookQueryBuilder;

@Service
@AllArgsConstructor
public class SearchQueryExecutor {

    private static final int PAGE_SIZE = 18;
    private ElasticsearchOperations elasticsearchOperations;

    public SearchHits<BookDocument> execute(Optional<List<String>> topicsFilter, int page) {
        var queryBuilder = bookQueryBuilder().withTopicsCriteria(topicsFilter).build();
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(page, PAGE_SIZE))
                .build();

        return elasticsearchOperations.search(searchQuery, BookDocument.class);
    }
}
