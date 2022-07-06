package com.productdock.library.search.application.port.in;

import com.productdock.library.search.domain.SearchBooksResultsPage;
import com.productdock.library.search.domain.SearchFilters;

public interface GetBooksQuery {
    SearchBooksResultsPage searchBooks(SearchFilters searchFilters, int page);
}
