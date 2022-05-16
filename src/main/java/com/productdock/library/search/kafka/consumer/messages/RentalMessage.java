package com.productdock.library.search.kafka.consumer.messages;

import com.productdock.library.search.book.BookStatus;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalMessage {

    private String bookId;
    @Singular
    private List<Record> rentalRecords;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Record {

        private String email;
        private BookStatus status;
    }

}
