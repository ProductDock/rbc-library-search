package com.productdock.library.search.kafka.cosumer.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsertBookMessage {

    public String id;
    public String title;
    public String cover;
    public String author;
    @Singular
    public List<BookTopic> topics;
}

