package com.productdock.library.search.data.provider;

import com.productdock.library.search.adapter.out.elastic.BookDocument;

import static com.productdock.library.search.data.provider.BookDocumentRatingMother.defaultBookRating;
import static com.productdock.library.search.data.provider.BookDocumentRentalStateMother.defaultRentalState;

public class BookDocumentMother {

    private static final String defaultId = null;
    private static final String defaultTitle = "::title::";
    private static final String defaultAuthor = "::author::";
    private static final String defaultCover = null;
    private static final boolean defaultRecommendation = false;

    public static BookDocument.BookDocumentBuilder defaultBookDocumentBuilder() {
        return BookDocument.builder()
                .bookId(defaultId)
                .title(defaultTitle)
                .author(defaultAuthor)
                .cover(defaultCover)
                .recommended(defaultRecommendation)
                .rentalState(defaultRentalState())
                .rating(defaultBookRating());
    }

    public static BookDocument defaultBookDocument() {
        return defaultBookDocumentBuilder().build();
    }

}
