package com.productdock.library.search.book;

import com.productdock.library.search.elastic.BookRatingMapper;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.productdock.library.search.data.provider.BookRatingMessageMother.defaultBookRatingMessage;

class BookRatingMapperShould {

    private final BookRatingMapper bookRatingMapper = Mappers.getMapper(BookRatingMapper.class);

    @Test
    void mapBookRatingMessageToBookDocumentRating() {
        var bookRatingMessage = defaultBookRatingMessage();

        var bookRating = bookRatingMapper.toRating(bookRatingMessage);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookRating.getScore()).isEqualTo(bookRatingMessage.getRating());
            softly.assertThat(bookRating.getCount()).isEqualTo(bookRatingMessage.getRatingsCount());
        }
    }
}
