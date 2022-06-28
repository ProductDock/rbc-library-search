package com.productdock.library.search.data.provider;


import com.productdock.library.search.adapter.in.kafka.messages.BookAvailabilityMessage;

public class BookAvailabilityMessageMother {

    private static final String defaultId = "1";
    private static final int defaultAvailableBookCount = 1;

    public static BookAvailabilityMessage.BookAvailabilityMessageBuilder defaultBookAvailabilityMessageBuilder() {
        return BookAvailabilityMessage.builder()
                .bookId(defaultId)
                .availableBookCount(defaultAvailableBookCount);
    }

    public static BookAvailabilityMessage defaultBookAvailabilityMessage() {
        return defaultBookAvailabilityMessageBuilder().build();
    }
}
