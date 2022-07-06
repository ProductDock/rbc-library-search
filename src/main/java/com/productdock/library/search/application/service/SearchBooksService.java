package com.productdock.library.search.application.service;

import com.productdock.library.search.application.port.in.GetBooksQuery;
import com.productdock.library.search.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.library.search.domain.SearchBooksResultsPage;
import com.productdock.library.search.domain.SearchFilters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public record SearchBooksService(BookPersistenceOutPort bookRepository) implements GetBooksQuery {

    @Override
    public SearchBooksResultsPage searchBooks(SearchFilters searchFilters, int page) {
        log.debug("Get books by search filters: {}", searchFilters);
        return bookRepository.searchBooksBy(searchFilters, page);
    }

}
