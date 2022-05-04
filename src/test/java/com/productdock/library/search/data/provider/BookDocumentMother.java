package com.productdock.library.search.data.provider;

import com.productdock.library.search.elastic.document.BookDocument;
import com.productdock.library.search.elastic.document.BookStatusWrapper;
import com.productdock.library.search.elastic.document.Record;

import java.util.ArrayList;
import java.util.List;

public class BookDocumentMother {

    private static final String defaultId = null;
    private static final String defaultTitle = "::title::";
    private static final String defaultAuthor = "::author::";
    private static final String defaultCover = null;

    private static final int defaultAvailableBookCount = 0;
    private static final List<Record> defaultRecords = new ArrayList<>();


    public static BookDocument.BookDocumentBuilder defaultBookDocumentBuilder() {
        return BookDocument.builder()
                .bookId(defaultId)
                .title(defaultTitle)
                .author(defaultAuthor)
                .cover(defaultCover)
                .bookStatusWrapper(new BookStatusWrapper(
                        defaultAvailableBookCount,
                        defaultRecords)
                );
    }

    public static BookDocument defaultBookDocument() {
        return defaultBookDocumentBuilder().build();
    }


}
