package com.productdock.library.search.kafka.cosumer.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookTopic {

    private String id;
    private String name;
}
