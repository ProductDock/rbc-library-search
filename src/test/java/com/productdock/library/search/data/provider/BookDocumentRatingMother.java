package com.productdock.library.search.data.provider;

import com.productdock.library.search.adapter.out.elastic.BookDocument;


public class BookDocumentRatingMother {

    private static final double defaultRating = 0;
    private static final int defaultRatingsCount = 0;


    public static BookDocument.Rating defaultBookRating() {
        return defaultBookRatingBuilder().build();
    }

    public static BookDocument.Rating.RatingBuilder defaultBookRatingBuilder() {
        return BookDocument.Rating.builder()
                .count(defaultRatingsCount)
                .score(defaultRating);
    }
}
