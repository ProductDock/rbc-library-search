package com.productdock.library.search;

import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public record BookService(BookIndexRepository bookIndexRepository,
                          SearchQueryExecutor searchQueryExecutor,
                          BookMapper bookMapper) {


    public SearchBooksResponse getBooks(Optional<List<String>> topics, int page) {
        SearchHits<BookIndex> hits = searchQueryExecutor.execute(topics, page);
        return new SearchBooksResponse(hits.getTotalHits(),
                bookMapper.toBookDtoCollection(hits.stream().map(hit -> hit.getContent()).toList()));
    }
}
