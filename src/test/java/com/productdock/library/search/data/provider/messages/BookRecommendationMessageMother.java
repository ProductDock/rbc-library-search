package com.productdock.library.search.data.provider.messages;

import com.productdock.library.search.adapter.in.kafka.messages.BookRecommendationMessage;

public class BookRecommendationMessageMother {

    public static BookRecommendationMessage.BookRecommendationMessageBuilder bookRecommendedBuilder() {
        return BookRecommendationMessage.builder()
                .recommended(true);
    }

}
