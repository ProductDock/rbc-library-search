package com.productdock.library.search.application.port.in;

import com.productdock.library.search.domain.ds.SearchBooksResult;
import com.productdock.library.search.domain.SearchFilters;

public interface GetBooksQuery {
    SearchBooksResult searchBooks(SearchFilters searchFilters, int page);
}
