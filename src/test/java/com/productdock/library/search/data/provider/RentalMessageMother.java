package com.productdock.library.search.data.provider;

import com.productdock.library.search.kafka.cosumer.messages.BookRecord;
import com.productdock.library.search.kafka.cosumer.messages.RentalMessage;

import java.util.ArrayList;
import java.util.Collection;

public class RentalMessageMother {

    private static final String defaultId = "1";
    private static final Collection<BookRecord> defaultRecords = new ArrayList<>();

    public static RentalMessage.RentalMessageBuilder defaultRentalMessageBuilder() {
        return RentalMessage.builder()
                .bookId(defaultId)
                .records(defaultRecords);
    }

    public static RentalMessage defaultRentalMessage() {
        return defaultRentalMessageBuilder().build();
    }
}
