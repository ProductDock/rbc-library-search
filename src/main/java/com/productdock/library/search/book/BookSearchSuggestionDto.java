package com.productdock.library.search.book;

import lombok.ToString;

@ToString
public class BookSearchSuggestionDto {
    public String id;
    public String title;
    public String author;
    public boolean recommended;
}
