package com.productdock.library.search.data.provider;

import com.productdock.library.search.adapter.out.elastic.BookDocument;
import com.productdock.library.search.domain.Book;

import java.util.ArrayList;

public class BookRentalMother {
    private static final int defaultAvailableBookCount = 0;

    public static Book.RentalState defaultBookRentalState() {
        return defaultRentalStateBuilder().build();
    }

    public static Book.RentalState.RentalStateBuilder defaultRentalStateBuilder() {
        return Book.RentalState.builder()
                .availableBooksCount(defaultAvailableBookCount)
                .records(new ArrayList<>());
    }
}
