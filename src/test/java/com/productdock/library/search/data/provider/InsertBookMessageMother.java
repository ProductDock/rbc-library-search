package com.productdock.library.search.data.provider;

import com.productdock.library.search.kafka.consumer.messages.InsertBookMessage;

public class InsertBookMessageMother {

    private static final String defaultId = "::id::";
    private static final String defaultTitle = "::title::";
    private static final String defaultAuthor = "::author::";
    private static final String defaultCover = "::cover::";

    private static final String defaultTopicId = "1";
    private static final String defaultTopicName = "::topic::";

    private static final InsertBookMessage.Topic defaultTopic =
            InsertBookMessage.Topic.builder().id(defaultTopicId).name(defaultTopicName).build();

    public static InsertBookMessage.InsertBookMessageBuilder defaultInsertBookMessageBuilder() {
        return InsertBookMessage.builder()
                .bookId(defaultId)
                .title(defaultTitle)
                .author(defaultAuthor)
                .cover(defaultCover)
                .topic(defaultTopic);
    }

    public static InsertBookMessage defaultInsertBookMessage() {
        return defaultInsertBookMessageBuilder().build();
    }
}
