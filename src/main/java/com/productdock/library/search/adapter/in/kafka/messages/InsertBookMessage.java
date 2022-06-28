package com.productdock.library.search.adapter.in.kafka.messages;

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
    private List<Topic> topics;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Topic {

        private String id;
        private String name;
    }
}

