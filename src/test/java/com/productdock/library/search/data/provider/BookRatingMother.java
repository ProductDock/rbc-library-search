package com.productdock.library.search.data.provider;

import com.productdock.library.search.adapter.out.elastic.BookDocument;
import com.productdock.library.search.domain.Book;

public class BookRatingMother {
    private static final double defaultRating = 0;
    private static final int defaultRatingsCount = 0;


    public static Book.Rating defaultBookDomainRating() {
        return defaultBookRatingBuilder().build();
    }

    public static Book.Rating.RatingBuilder defaultBookRatingBuilder() {
        return Book.Rating.builder()
                .count(defaultRatingsCount)
                .score(defaultRating);
    }
}
