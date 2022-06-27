package com.productdock.library.search.clean;

import com.productdock.library.search.elastic.document.BookDocument;

import java.util.List;

public interface SuggestBooksQuery {
    List<BookDocument> searchBookSuggestions(SearchFilters searchFilters);
}
