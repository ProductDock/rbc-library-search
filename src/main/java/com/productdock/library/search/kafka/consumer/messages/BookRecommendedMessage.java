package com.productdock.library.search.kafka.consumer.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRecommendedMessage {

    private String bookId;
}
