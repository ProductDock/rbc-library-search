package com.productdock.library.search.clean;

public interface FindBooksQuery {
    SearchBooksResult getBooks(SearchFilters searchFilters, int page);
}
