package com.productdock.library.search.book;

import com.productdock.library.search.elastic.document.Record;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static com.productdock.library.search.data.provider.BookDocumentMother.defaultBookDocumentBuilder;
import static com.productdock.library.search.data.provider.InsertBookMessageMother.defaultInsertBookMessageBuilder;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BookMapperImpl.class, RecordDtoMapperImpl.class})
class BookMapperShould {

    @Autowired
    private BookMapper bookMapper;

    @Test
    void mapBookDocumentToBookDto() {
        var bookDocument = defaultBookDocumentBuilder()
                .id("123").title("Book title").author("Book author").cover("Book cover").build();
        List<Record> records = new ArrayList<>();
        Record record = new Record();
        record.setEmail("natasa@gmail.com");
        record.setStatus(BookStatus.RENTED);
        records.add(record);
        bookDocument.setRecords(records);

        var bookDto = bookMapper.toBookDto(bookDocument);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookDto.id).isEqualTo(bookDocument.getId());
            softly.assertThat(bookDto.title).isEqualTo(bookDocument.getTitle());
            softly.assertThat(bookDto.author).isEqualTo(bookDocument.getAuthor());
            softly.assertThat(bookDto.cover).isEqualTo(bookDocument.getCover());
        }
    }

    @Test
    void mapInsertBookMessageToBookDocument() {
        var insertBookMessage = defaultInsertBookMessageBuilder()
                .id("123").title("Book title").author("Book author").cover("Book cover").build();

        var bookDocument = bookMapper.toBookDocument(insertBookMessage);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookDocument.getId()).isEqualTo(insertBookMessage.getId());
            softly.assertThat(bookDocument.getTitle()).isEqualTo(insertBookMessage.getTitle());
            softly.assertThat(bookDocument.getAuthor()).isEqualTo(insertBookMessage.getAuthor());
            softly.assertThat(bookDocument.getCover()).isEqualTo(insertBookMessage.getCover());
            softly.assertThat(bookDocument.getTopics())
                    .extracting("id", "name")
                    .containsExactly(tuple("1", "::topic::"));

        }
    }
}
