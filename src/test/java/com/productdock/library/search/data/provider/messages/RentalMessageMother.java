package com.productdock.library.search.data.provider.messages;

import com.productdock.library.search.adapter.in.kafka.messages.RentalMessage;

import java.util.ArrayList;
import java.util.Collection;

public class RentalMessageMother {

    private static final String defaultId = "1";
    private static final Collection<RentalMessage.Record> defaultRecords = new ArrayList<>();

    public static RentalMessage.RentalMessageBuilder rentalMessageBuilder() {
        return RentalMessage.builder()
                .bookId(defaultId)
                .rentalRecords(defaultRecords);
    }

}
