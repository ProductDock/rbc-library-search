package com.productdock.library.search.data.provider.messages;

import com.productdock.library.search.adapter.in.kafka.messages.RentalMessage;
import com.productdock.library.search.domain.BookStatus;

public class RentalRecordMessageMother {

    public static RentalMessage.Record.RecordBuilder rentedRecordBuilder() {
        return RentalMessage.Record.builder().status(BookStatus.RENTED);
    }

}
