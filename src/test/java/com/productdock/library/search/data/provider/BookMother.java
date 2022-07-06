package com.productdock.library.search.data.provider;

import com.productdock.library.search.domain.Book;

import java.util.ArrayList;
import java.util.List;

import static com.productdock.library.search.data.provider.BookRatingMother.defaultBookDomainRating;
import static com.productdock.library.search.data.provider.BookRentalMother.defaultBookRentalState;

public class BookMother {
    private static final String defaultId = null;
    private static final String defaultTitle = "::title::";
    private static final String defaultAuthor = "::author::";
    private static final String defaultCover = null;
    private static final boolean defaultRecommendation = false;
    private static final List<Book.Topic> defaultTopics = new ArrayList<>();

    public static Book.BookBuilder defaultBookBuilder() {
        return Book.builder()
                .id(defaultId)
                .title(defaultTitle)
                .author(defaultAuthor)
                .cover(defaultCover)
                .recommended(defaultRecommendation)
                .topics(defaultTopics)
                .rentalState(defaultBookRentalState())
                .rating(defaultBookDomainRating());
    }

    public static Book defaultBook() {
        return defaultBookBuilder().build();
    }

}
