package com.productdock.library.search.application.port.in;

import com.productdock.library.search.domain.SearchFilters;
import com.productdock.library.search.domain.Book;

import java.util.List;

public interface GetSuggestedBooksQuery {
    List<Book> searchBookSuggestions(SearchFilters searchFilters);
}
