package com.productdock.library.search.adapter.in.kafka.messages;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookAvailabilityMessage {

    private String bookId;
    private int availableBookCount;
}
