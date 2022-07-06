package com.productdock.library.search.application.port.out.persistence;

import com.productdock.library.search.domain.Book;
import com.productdock.library.search.domain.SearchBooksResultsPage;
import com.productdock.library.search.domain.SearchFilters;

import java.util.List;
import java.util.Optional;

public interface BookPersistenceOutPort {
    Optional<Book> findById(String bookId);

    void save(Book book);

    List<Book> searchBooksBy(SearchFilters searchFilters);

    SearchBooksResultsPage searchBooksBy(SearchFilters searchFilters, int page);
}
