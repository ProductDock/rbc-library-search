package com.productdock.library.search.kafka.cosumer.messages;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookTopic {

    private String id;
    private String name;
}
