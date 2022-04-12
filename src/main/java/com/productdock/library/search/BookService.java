package com.productdock.library.search;

import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public record BookService(BookIndexRepository bookIndexRepository,
                          SearchQueryExecutor searchQueryExecutor,
                          BookMapper bookMapper) {


    public CountableCollectionDto getBooks(Optional<List<String>> topics, int page) {
        List<String> topicsForQuery = new ArrayList<>();
        if (topics.isPresent())
            topicsForQuery = topics.get();

        SearchHits<BookIndex> hits = searchQueryExecutor.execute(topicsForQuery,page);
        return bookMapper.toCountableCollectionDto(hits.getTotalHits(),
                hits.stream().map(hit -> bookMapper.toBookDto(hit.getContent())).toList());
    }
}
