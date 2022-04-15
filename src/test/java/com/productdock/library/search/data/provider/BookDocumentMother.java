package com.productdock.library.search.data.provider;

import com.productdock.library.search.elastic.document.BookDocument;

public class BookDocumentMother {

    private static final String defaultId = null;
    private static final String defaultTitle = "::title::";
    private static final String defaultAuthor = "::author::";
    private static final String defaultCover = null;

    public static BookDocument.BookDocumentBuilder defaultBookBuilder() {
        return BookDocument.builder()
                .id(defaultId)
                .title(defaultTitle)
                .author(defaultAuthor)
                .cover(defaultCover);
    }

    public static BookDocument defaultBook() {
        return defaultBookBuilder().build();
    }


}
