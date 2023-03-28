package com.productdock.library.search.adapter.out.elastic;

import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.productdock.library.search.data.provider.BookDocumentMother.defaultBookDocument;
import static com.productdock.library.search.data.provider.BookMother.defaultBook;

class BookDocumentMapperShould {

    private BookDocumentMapper bookDocumentMapper = Mappers.getMapper(BookDocumentMapper.class);

    @Test
    void mapBookDocumentToBook() {
        var bookDocument = defaultBookDocument();

        var book = bookDocumentMapper.toDomain(bookDocument);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(book.getId()).isEqualTo(bookDocument.getBookId());
            softly.assertThat(book.getAuthor()).isEqualTo(bookDocument.getAuthor());
            softly.assertThat(book.getTitle()).isEqualTo(bookDocument.getTitle());
            softly.assertThat(book.getCover()).isEqualTo(bookDocument.getCover());
            softly.assertThat(book.getRentalState().getAvailableBooksCount()).isEqualTo(bookDocument.getRentalState().getAvailableBooksCount());
        }
    }

    @Test
    void mapBookToBookDocument() {
        var book = defaultBook();

        var bookDocument = bookDocumentMapper.toEntity(book);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookDocument.getBookId()).isEqualTo(book.getId());
            softly.assertThat(bookDocument.getAuthor()).isEqualTo(book.getAuthor());
            softly.assertThat(bookDocument.getTitle()).isEqualTo(book.getTitle());
            softly.assertThat(bookDocument.getCover()).isEqualTo(book.getCover());
            softly.assertThat(bookDocument.getRentalState().getAvailableBooksCount()).isEqualTo(book.getRentalState().getAvailableBooksCount());
        }
    }
}
