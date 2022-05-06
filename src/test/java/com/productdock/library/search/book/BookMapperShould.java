package com.productdock.library.search.book;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.productdock.library.search.data.provider.BookDocumentMother.defaultBookDocumentBuilder;
import static com.productdock.library.search.data.provider.BookDocumentRentalStateMother.defaultRentalStateBuilder;
import static com.productdock.library.search.data.provider.BookDocumentRentalStateRecordMother.defaultRecord;
import static com.productdock.library.search.data.provider.InsertBookMessageMother.defaultInsertBookMessageBuilder;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BookMapper.class, BookDtoRecordMapperImpl.class})
class BookMapperShould {

    @Autowired
    private BookMapper bookMapper;

    @Test
    void mapBookDocumentToBookDto() {
        var bookDocument = defaultBookDocumentBuilder()
                .rentalState(defaultRentalStateBuilder()
                        .record(defaultRecord()).build()).build();

        var bookDto = bookMapper.toBookDto(bookDocument);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookDto.id).isEqualTo(bookDocument.getBookId());
            softly.assertThat(bookDto.title).isEqualTo(bookDocument.getTitle());
            softly.assertThat(bookDto.author).isEqualTo(bookDocument.getAuthor());
            softly.assertThat(bookDto.cover).isEqualTo(bookDocument.getCover());
            softly.assertThat(bookDto.records)
                    .extracting("email", "status")
                    .containsExactly(tuple("::email::", BookStatus.RENTED));
        }
    }

    @Test
    void mapBookDocumentToBookDto_whenBookStatusWrapperHasOneRecordAndOneAvailableBookCount() {
        var bookDocument =
                defaultBookDocumentBuilder()
                        .bookId("123")
                        .cover("Book cover")
                        .rentalState(defaultRentalStateBuilder()
                                .availableBooksCount(1)
                                .record(defaultRecord())
                                .build()).build();

        var bookDto = bookMapper.toBookDto(bookDocument);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookDto.id).isEqualTo(bookDocument.getBookId());
            softly.assertThat(bookDto.title).isEqualTo(bookDocument.getTitle());
            softly.assertThat(bookDto.author).isEqualTo(bookDocument.getAuthor());
            softly.assertThat(bookDto.cover).isEqualTo(bookDocument.getCover());
            softly.assertThat(bookDto.records).hasSize(2);
            softly.assertThat(bookDto.records)
                    .extracting("email", "status")
                    .containsExactlyInAnyOrder(
                            tuple(null, BookStatus.AVAILABLE),
                            tuple("::email::", BookStatus.RENTED)
                    );
        }
    }


    @Test
    void mapInsertBookMessageToBookDocument() {
        var insertBookMessage = defaultInsertBookMessageBuilder().build();

        var bookDocument = bookMapper.toBookDocument(insertBookMessage);

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
