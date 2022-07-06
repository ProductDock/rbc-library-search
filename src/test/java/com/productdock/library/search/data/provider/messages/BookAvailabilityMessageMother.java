package com.productdock.library.search.data.provider.messages;


import com.productdock.library.search.adapter.in.kafka.messages.BookAvailabilityMessage;

public class BookAvailabilityMessageMother {

    public static BookAvailabilityMessage.BookAvailabilityMessageBuilder bookAvailabilityBuilder() {
        return BookAvailabilityMessage.builder();
    }

}
