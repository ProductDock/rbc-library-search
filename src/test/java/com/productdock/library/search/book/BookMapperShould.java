package com.productdock.library.search.book;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.productdock.library.search.data.provider.BookDocumentMother.defaultBookDocumentBuilder;
import static com.productdock.library.search.data.provider.InsertBookMessageMother.defaultInsertBookMessageBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BookMapperImpl.class})
public class BookMapperShould {

    @Autowired
    private BookMapper bookMapper;

    @Test
    void mapBookDocumentToBookDto() {
        var bookDocument = defaultBookDocumentBuilder()
                .id("123").title("Book title").author("Book author").cover("Book cover").build();

        var bookDto = bookMapper.toBookDto(bookDocument);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookDto.id).isEqualTo(bookDocument.id);
            softly.assertThat(bookDto.title).isEqualTo(bookDocument.title);
            softly.assertThat(bookDto.author).isEqualTo(bookDocument.author);
            softly.assertThat(bookDto.cover).isEqualTo(bookDocument.cover);
        }
    }

    @Test
    void mapInsertBookMessageToBookDocument() {
        var insertBookMessage = defaultInsertBookMessageBuilder()
                .id("123").title("Book title").author("Book author").cover("Book cover").build();

        var bookDocument = bookMapper.toBookDocument(insertBookMessage);

        assertThat(bookDocument.id).isEqualTo(insertBookMessage.id);
        assertThat(bookDocument.title).isEqualTo(insertBookMessage.title);
        assertThat(bookDocument.author).isEqualTo(insertBookMessage.author);
        assertThat(bookDocument.cover).isEqualTo(insertBookMessage.cover);

        assertThat(bookDocument.topics)
                .extracting("id", "name")
                .containsExactly(tuple("1", "::topic::"));

    }
}
