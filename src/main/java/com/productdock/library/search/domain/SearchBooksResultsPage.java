package com.productdock.library.search.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Builder
public class SearchBooksResultsPage {

    public final long count;

    public final Iterable<Book> books;
}
