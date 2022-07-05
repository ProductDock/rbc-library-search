package com.productdock.library.search.domain.ds;

import com.productdock.library.search.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Builder
public class SearchBooksResult {

    public final long count;

    public final Iterable<Book> books;
}
