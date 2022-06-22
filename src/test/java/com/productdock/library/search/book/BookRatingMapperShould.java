package com.productdock.library.search.book;

import com.productdock.library.search.elastic.BookRatingMapper;
import com.productdock.library.search.elastic.BookRatingMapperImpl;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.productdock.library.search.data.provider.BookRatingMessageMother.defaultBookRatingMessage;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BookRatingMapperImpl.class})
class BookRatingMapperShould {
//
//    @Autowired
//    private BookRatingMapper bookRatingMapper;
//
//    @Test
//    void mapBookRatingMessageToBookDocumentRating() {
//        var bookRatingMessage = defaultBookRatingMessage();
//
//        var bookRating = bookRatingMapper.toRating(bookRatingMessage);
//
//        try (var softly = new AutoCloseableSoftAssertions()) {
//            softly.assertThat(bookRating.getScore()).isEqualTo(bookRatingMessage.getRating());
//            softly.assertThat(bookRating.getCount()).isEqualTo(bookRatingMessage.getRatingsCount());
//        }
//    }
}
