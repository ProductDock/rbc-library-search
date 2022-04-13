package com.productdock.library.search;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchBooksResponse {

    @NonNull
    public long count;
    @NonNull
    public Iterable<BookDto> books;
}
