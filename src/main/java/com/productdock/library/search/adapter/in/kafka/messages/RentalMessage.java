package com.productdock.library.search.adapter.in.kafka.messages;

import com.productdock.library.search.domain.BookStatus;
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

        private String patron;
        private BookStatus status;
    }

}
