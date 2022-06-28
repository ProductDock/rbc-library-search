package com.productdock.library.search.application.service;

import com.productdock.library.search.application.port.in.GetSuggestedBooksQuery;
import com.productdock.library.search.application.port.out.persistence.BookDocumentPersistenceOutPort;
import com.productdock.library.search.domain.SearchFilters;
import com.productdock.library.search.domain.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public record SuggestBooksService(BookDocumentPersistenceOutPort bookRepository) implements GetSuggestedBooksQuery {

    @Override
    public List<Book> searchBookSuggestions(SearchFilters searchFilters) {
        log.debug("Get book suggestions by search text: {}", searchFilters.getSearchText());
        return bookRepository.searchBooksBy(searchFilters);
    }
}
