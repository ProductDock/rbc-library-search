package com.productdock.library.search.data.provider.messages;

import com.productdock.library.search.adapter.in.kafka.messages.BookRatingMessage;

public class BookRatingMessageMother {

    public static BookRatingMessage.BookRatingMessageBuilder bookRatingBuilder() {
        return BookRatingMessage.builder();
    }

}
