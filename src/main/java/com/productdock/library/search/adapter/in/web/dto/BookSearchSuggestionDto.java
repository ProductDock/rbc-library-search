package com.productdock.library.search.adapter.in.web.dto;

import lombok.ToString;

@ToString
public class BookSearchSuggestionDto {
    public String id;
    public String title;
    public String author;
    public boolean recommended;
}
