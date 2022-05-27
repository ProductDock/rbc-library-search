package com.productdock.library.search.book;

public enum BookSearchFields {
    TOPICS("topics.name"),
    RECOMMENDED("recommended");

    public final String label;

    BookSearchFields(String label) {
        this.label = label;
    }
}
