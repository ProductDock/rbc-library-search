package com.productdock.library.search.book;


import com.productdock.library.search.elastic.document.BookDocument;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.productdock.library.search.data.provider.BookDocumentMother.defaultBookDocumentBuilder;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BookSearchSuggestionDtoMapperImpl.class})
class BookSearchSuggestionDtoMapperShould {

    @Autowired
    private BookSearchSuggestionDtoMapper bookSearchSuggestionDtoMapper;

    @Test
    void mapRentalStateRecordToBookDtoRecord() {
        var bookDocument = defaultBookDocumentBuilder().build();

        var bookSearchSuggestionDto = bookSearchSuggestionDtoMapper.toBookSearchSuggestionDto(bookDocument);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookSearchSuggestionDto.id).isEqualTo(bookDocument.getBookId());
            softly.assertThat(bookSearchSuggestionDto.title).isEqualTo(bookDocument.getTitle());
            softly.assertThat(bookSearchSuggestionDto.author).isEqualTo(bookDocument.getAuthor());
            softly.assertThat(bookSearchSuggestionDto.recommended).isEqualTo(bookDocument.isRecommended());
        }
    }
}
