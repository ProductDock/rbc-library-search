package com.productdock.library.search.kafka.cosumer.messages;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalMessage {

    private String bookId;
    @Singular
    private List<BookRecord> records;

}
