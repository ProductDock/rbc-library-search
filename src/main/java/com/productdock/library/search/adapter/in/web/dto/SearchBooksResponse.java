package com.productdock.library.search.adapter.in.web.dto;

import com.productdock.library.search.adapter.in.web.dto.BookDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchBooksResponse {

    @NonNull
    public long count;
    @NonNull
    public Iterable<BookDto> books;
}
