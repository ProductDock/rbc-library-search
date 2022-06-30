package com.productdock.library.search.application.port.in;

import com.productdock.library.search.domain.SearchFilters;
import com.productdock.library.search.domain.ds.SearchBooksResult;

public interface GetBooksQuery {
    SearchBooksResult searchBooks(SearchFilters searchFilters, int page);
}
