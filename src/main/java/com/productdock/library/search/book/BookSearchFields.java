package com.productdock.library.search.book;

public enum BookSearchFields {
    TOPICS_NAME("topics.name");

    public final String label;

    BookSearchFields(String label) {
        this.label = label;
    }
}