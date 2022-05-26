package com.productdock.library.search.data.provider;

import com.productdock.library.search.kafka.consumer.messages.BookRecommendationMessage;

public class BookRecommendationMessageMother {

    private static final String defaultId = "1";
    private static final Boolean defaultRecommendation = null;

    public static BookRecommendationMessage.BookRecommendationMessageBuilder defaultBookRecommendationMessageBuilder() {
        return BookRecommendationMessage.builder()
                .bookId(defaultId)
                .recommendation(defaultRecommendation);
    }

    public static BookRecommendationMessage defaultBookRecommendationMessage() {
        return defaultBookRecommendationMessageBuilder().build();
    }
}
