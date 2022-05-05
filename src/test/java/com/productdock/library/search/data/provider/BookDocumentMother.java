package com.productdock.library.search.data.provider;

import com.productdock.library.search.book.BookStatus;
import com.productdock.library.search.elastic.document.BookDocument;


public class BookDocumentMother {

    private static final String defaultId = null;
    private static final String defaultTitle = "::title::";
    private static final String defaultAuthor = "::author::";
    private static final String defaultCover = null;

    private static final int defaultAvailableBookCount = 0;
    private static final BookDocument.RentalState.Record defaultRecord =
            new BookDocument.RentalState.Record("::email::", BookStatus.RENTED);


    public static BookDocument.BookDocumentBuilder defaultBookDocumentBuilder() {
        return BookDocument.builder()
                .bookId(defaultId)
                .title(defaultTitle)
                .author(defaultAuthor)
                .cover(defaultCover)
                .rentalState(defaultBookDocumentRentalState());
    }

    public static BookDocument.RentalState.RentalStateBuilder defaultBookDocumentRentalStateBuilder() {
        return BookDocument.RentalState.builder()
                .availableBooksCount(defaultAvailableBookCount)
                .record(defaultRecord);
    }

    public static BookDocument.RentalState defaultBookDocumentRentalState() {
        return defaultBookDocumentRentalStateBuilder().build();
    }

    public static BookDocument defaultBookDocument() {
        return defaultBookDocumentBuilder().build();
    }


}
