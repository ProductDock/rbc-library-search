package com.productdock.library.search.data.provider;

import com.productdock.library.search.BookIndex;

public class BookIndexMother {

    private static final String defaultId = null;
    private static final String defaultTitle = "::title::";
    private static final String defaultAuthor = "::author::";
    private static final String defaultCover = null;

    public static BookIndex.BookIndexBuilder defaultBookBuilder() {
        return BookIndex.builder()
                .id(defaultId)
                .title(defaultTitle)
                .author(defaultAuthor)
                .cover(defaultCover);
    }

    public static BookIndex defaultBook() {
        return defaultBookBuilder().build();
    }


}
