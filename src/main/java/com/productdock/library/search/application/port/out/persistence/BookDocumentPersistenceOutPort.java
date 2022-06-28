package com.productdock.library.search.application.port.out.persistence;

import com.productdock.library.search.domain.SearchFilters;
import com.productdock.library.search.domain.Book;
import com.productdock.library.search.domain.ds.SearchBooksResult;

import java.util.List;
import java.util.Optional;

public interface BookDocumentPersistenceOutPort {
    Optional<Book> findById(String bookId);

    void save(Book book);

    List<Book> searchBooksBy(SearchFilters searchFilters);

    SearchBooksResult searchBooksBy(SearchFilters searchFilters, int page);
}
