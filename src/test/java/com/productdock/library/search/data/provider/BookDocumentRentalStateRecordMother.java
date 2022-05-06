package com.productdock.library.search.data.provider;

import com.productdock.library.search.book.BookStatus;
import com.productdock.library.search.elastic.document.BookDocument;


public class BookDocumentRentalStateRecordMother {

    private static final String defaultEmail = "::email::";
    private static final BookStatus defaultBookStatus = BookStatus.RENTED;

    public static BookDocument.RentalState.Record defaultRecord() {
        return defaultRecordBuilder().build();
    }

    public static BookDocument.RentalState.Record.RecordBuilder defaultRecordBuilder() {
        return BookDocument.RentalState.Record.builder()
                .email(defaultEmail)
                .status(defaultBookStatus);
    }

}
