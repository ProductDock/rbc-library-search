package com.productdock.library.search.clean;

public enum BookSearchFields {
    TOPICS("topics.name"),
    RECOMMENDED("recommended"),
    TITLE("title"),
    AUTHOR("author");

    public final String label;

    BookSearchFields(String label) {
        this.label = label;
    }
}