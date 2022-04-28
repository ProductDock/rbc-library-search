package com.productdock.library.search.kafka.cosumer.messages;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InsertBookMessage {

    private String bookId;
    private String title;
    private String cover;
    private String author;
    @Singular
    private List<BookTopic> topics;
}

