package com.productdock.library.search.adapter.in.kafka.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRecommendationMessage {

    private String bookId;
    private Boolean recommended;
}
