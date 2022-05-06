package com.productdock.library.search.kafka.consumer.messages;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookAvailabilityMessage {

    private String bookId;
    private int availableBookCount;
}
