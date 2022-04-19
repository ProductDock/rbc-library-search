package com.productdock.library.search.kafka.cosumer.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookTopic {

    public String id;
    public String name;
}
