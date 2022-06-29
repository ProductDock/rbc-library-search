package com.productdock.library.search.adapter.in.web;


import com.productdock.library.search.adapter.in.web.mapper.BookSearchSuggestionDtoMapper;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.productdock.library.search.data.provider.BookMother.defaultBookBuilder;

class BookSearchSuggestionDtoMapperShould {

    private final BookSearchSuggestionDtoMapper bookSearchSuggestionDtoMapper = Mappers.getMapper(BookSearchSuggestionDtoMapper.class);

    @Test
    void mapRentalStateRecordToBookDtoRecord() {
        var book = defaultBookBuilder().build();

        var bookSearchSuggestionDto = bookSearchSuggestionDtoMapper.toDto(book);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookSearchSuggestionDto.id).isEqualTo(book.getId());
            softly.assertThat(bookSearchSuggestionDto.title).isEqualTo(book.getTitle());
            softly.assertThat(bookSearchSuggestionDto.author).isEqualTo(book.getAuthor());
            softly.assertThat(bookSearchSuggestionDto.recommended).isEqualTo(book.isRecommended());
        }
    }
}
