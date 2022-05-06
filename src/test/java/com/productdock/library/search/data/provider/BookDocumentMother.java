package com.productdock.library.search.data.provider;

import com.productdock.library.search.book.BookStatus;
import com.productdock.library.search.elastic.document.BookDocument;

import static com.productdock.library.search.data.provider.BookDocumentRentalStateMother.defaultRentalState;


public class BookDocumentMother {

    private static final String defaultId = null;
    private static final String defaultTitle = "::title::";
    private static final String defaultAuthor = "::author::";
    private static final String defaultCover = null;

    public static BookDocument.BookDocumentBuilder defaultBookDocumentBuilder() {
        return BookDocument.builder()
                .bookId(defaultId)
                .title(defaultTitle)
                .author(defaultAuthor)
                .cover(defaultCover)
                .rentalState(defaultRentalState());
    }

    public static BookDocument defaultBookDocument() {
        return defaultBookDocumentBuilder().build();
    }

}
