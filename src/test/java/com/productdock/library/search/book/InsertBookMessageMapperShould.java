package com.productdock.library.search.book;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.productdock.library.search.data.provider.InsertBookMessageMother.defaultInsertBookMessageBuilder;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

class InsertBookMessageMapperShould {

    private final InsertBookMessageMapper insertBookMessageMapper = Mappers.getMapper(InsertBookMessageMapper.class);

    @Test
    void mapInsertBookMessageToBookDocument() {
        var insertBookMessage = defaultInsertBookMessageBuilder().build();

        var bookDocument = insertBookMessageMapper.toBookDocument(insertBookMessage);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookDocument.getBookId()).isEqualTo(insertBookMessage.getBookId());
            softly.assertThat(bookDocument.getTitle()).isEqualTo(insertBookMessage.getTitle());
            softly.assertThat(bookDocument.getAuthor()).isEqualTo(insertBookMessage.getAuthor());
            softly.assertThat(bookDocument.getCover()).isEqualTo(insertBookMessage.getCover());
            softly.assertThat(bookDocument.getTopics())
                    .extracting("id", "name")
                    .containsExactly(tuple("1", "::topic::"));

        }
    }
}
