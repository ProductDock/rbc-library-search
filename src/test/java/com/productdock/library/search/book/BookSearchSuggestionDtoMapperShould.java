package com.productdock.library.search.book;


import com.productdock.library.search.clean.BookSearchSuggestionDtoMapper;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.productdock.library.search.data.provider.BookDocumentMother.defaultBookDocumentBuilder;

class BookSearchSuggestionDtoMapperShould {

    private final BookSearchSuggestionDtoMapper bookSearchSuggestionDtoMapper = Mappers.getMapper(BookSearchSuggestionDtoMapper.class);

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
