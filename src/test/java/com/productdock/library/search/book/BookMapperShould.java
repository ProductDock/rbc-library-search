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
            softly.assertThat(bookDto.rating.count).isEqualTo(bookDocument.getRating().getCount());
            softly.assertThat(bookDto.rating.score).isEqualTo(bookDocument.getRating().getScore());
            softly.assertThat(bookDto.records)
                    .extracting("email", "status")
                    .containsExactly(tuple("::email::", BookStatus.RENTED));
        }
    }

    @Test
    void mapBookDocumentToBookDto_whenRatingNotPresent() {
        var bookDocument = defaultBookDocumentBuilder()
                .rating(null)
                .build();

        var bookDto = bookMapper.toBookDto(bookDocument);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookDto.rating.count).isNull();
            softly.assertThat(bookDto.rating.score).isNull();
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
}
