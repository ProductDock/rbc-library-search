package com.productdock.library.search.data.provider;

import com.productdock.library.search.kafka.consumer.messages.RentalMessage;

import java.util.ArrayList;
import java.util.Collection;

public class RentalMessageMother {

    private static final String defaultId = "1";
    private static final Collection<RentalMessage.Record> defaultRecords = new ArrayList<>();

    public static RentalMessage.RentalMessageBuilder defaultRentalMessageBuilder() {
        return RentalMessage.builder()
                .bookId(defaultId)
                .rentalRecords(defaultRecords);
    }

    public static RentalMessage defaultRentalMessage() {
        return defaultRentalMessageBuilder().build();
    }
}
