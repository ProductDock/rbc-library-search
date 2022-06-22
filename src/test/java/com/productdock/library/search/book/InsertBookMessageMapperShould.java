package com.productdock.library.search.book;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.productdock.library.search.data.provider.InsertBookMessageMother.defaultInsertBookMessageBuilder;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {InsertBookMessageMapperImpl.class})
class InsertBookMessageMapperShould {

    @Autowired
    private InsertBookMessageMapper insertBookMessageMapper;

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
