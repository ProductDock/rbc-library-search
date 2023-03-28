package com.productdock.library.search.adapter.in.kafka;

import com.productdock.library.search.adapter.in.kafka.mappers.InsertBookMessageMapper;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static com.productdock.library.search.data.provider.messages.InsertBookMessageMother.defaultInsertBookMessageBuilder;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

class InsertBookMessageMapperShould {

    private final InsertBookMessageMapper insertBookMessageMapper = Mappers.getMapper(InsertBookMessageMapper.class);

    @Test
    void mapInsertBookMessageToBookDocument() {
        var insertBookMessage = defaultInsertBookMessageBuilder().build();

        var book = insertBookMessageMapper.toBook(insertBookMessage);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(book.getId()).isEqualTo(insertBookMessage.getBookId());
            softly.assertThat(book.getTitle()).isEqualTo(insertBookMessage.getTitle());
            softly.assertThat(book.getAuthor()).isEqualTo(insertBookMessage.getAuthor());
            softly.assertThat(book.getCover()).isEqualTo(insertBookMessage.getCover());
            softly.assertThat(book.getTopics())
                    .extracting("id", "name")
                    .containsExactly(tuple("1", "::topic::"));
            softly.assertThat(book.getRentalState().getAvailableBooksCount()).isEqualTo(insertBookMessage.getBookCopies());
        }
    }
}
