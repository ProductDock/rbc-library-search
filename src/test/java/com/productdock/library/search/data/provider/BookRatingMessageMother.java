package com.productdock.library.search.data.provider;

import com.productdock.library.search.kafka.consumer.messages.BookRatingMessage;

public class BookRatingMessageMother {

    private static final String defaultBookId = "1";
    private static final double defaultRating = 3.5;
    private static final int defaultRatingsCount = 2;

    public static BookRatingMessage.BookRatingMessageBuilder defaultBookRatingMessageBuilder() {
        return BookRatingMessage.builder()
                .bookId(defaultBookId)
                .ratingsCount(defaultRatingsCount)
                .rating(defaultRating);
    }

    public static BookRatingMessage defaultBookRatingMessage() {
        return defaultBookRatingMessageBuilder().build();
    }
}
