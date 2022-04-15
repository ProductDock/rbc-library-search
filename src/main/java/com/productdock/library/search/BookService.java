package com.productdock.library.search;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public record BookService(BookIndexRepository bookIndexRepository,
                          SearchQueryExecutor searchQueryExecutor,
                          BookMapper bookMapper) {


    public SearchBooksResponse getBooks(Optional<List<String>> topics, int page) {
        var hits = searchQueryExecutor.execute(topics, page);
        var bookHitsDto = hits.stream().map(hit -> bookMapper.toBookDto(hit.getContent())).toList();
        return new SearchBooksResponse(hits.getTotalHits(), bookHitsDto);
    }

    public void save(BookIndex bookIndex) {
        bookIndexRepository.save(bookIndex);
    }
}
