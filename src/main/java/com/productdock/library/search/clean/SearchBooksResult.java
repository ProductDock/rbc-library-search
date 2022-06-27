package com.productdock.library.search.clean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchBooksResult {

    private long count;

    private Iterable<Book> books;
}
