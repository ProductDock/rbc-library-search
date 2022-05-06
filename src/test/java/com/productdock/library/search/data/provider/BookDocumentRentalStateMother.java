package com.productdock.library.search.data.provider;

import com.productdock.library.search.elastic.document.BookDocument;

import java.util.ArrayList;

public class BookDocumentRentalStateMother {

    private static final int defaultAvailableBookCount = 0;

    public static BookDocument.RentalState defaultRentalState() {
        return defaultRentalStateBuilder().build();
    }

    public static BookDocument.RentalState.RentalStateBuilder defaultRentalStateBuilder() {
        return BookDocument.RentalState.builder()
                .availableBooksCount(defaultAvailableBookCount)
                .records(new ArrayList<>());
    }

}
