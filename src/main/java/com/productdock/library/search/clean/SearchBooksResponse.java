package com.productdock.library.search.clean;

import com.productdock.library.search.clean.BookDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchBooksResponse {

    @NonNull
    public long count;
    @NonNull
    public Iterable<BookDto> books;
}
