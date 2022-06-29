package com.productdock.library.search.adapter.in.web.dto;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@NoArgsConstructor
public class SearchBooksResponse {

    @NonNull
    public long count;
    @NonNull
    public List<BookDto> books = new ArrayList<>();
}
