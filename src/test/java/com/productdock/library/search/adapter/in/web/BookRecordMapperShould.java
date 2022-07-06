package com.productdock.library.search.adapter.in.web;


import com.productdock.library.search.adapter.in.web.mapper.BookRecordMapper;
import com.productdock.library.search.domain.Book;
import com.productdock.library.search.domain.BookStatus;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

import static java.util.stream.Stream.of;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

class BookRecordMapperShould {

    private BookRecordMapper bookRecordMapper = Mappers.getMapper(BookRecordMapper.class);

    @Test
    void mapRentalStateRecordToBookDtoRecord() {
        var rentalStateRecord = Book.RentalState.Record.builder()
                .email("email1").status(BookStatus.AVAILABLE).build();

        var bookDtoRecord = bookRecordMapper.toRecord(rentalStateRecord);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookDtoRecord.email).isEqualTo(rentalStateRecord.getEmail());
            softly.assertThat(bookDtoRecord.status).isEqualTo(rentalStateRecord.getStatus());
        }
    }

    @Test
    void mapRentalStateRecordsToBookDtoRecords() {
        var rentalStateRecords = of(
                Book.RentalState.Record.builder().email("email1").status(BookStatus.AVAILABLE).build(),
                Book.RentalState.Record.builder().email("email2").status(BookStatus.RENTED).build()
        ).collect(Collectors.toList());

        var bookDtoRecords = bookRecordMapper.toRecords(rentalStateRecords);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookDtoRecords)
                    .extracting("email", "status")
                    .containsExactlyInAnyOrder(
                            tuple("email1", BookStatus.AVAILABLE),
                            tuple("email2", BookStatus.RENTED)
                    );
        }
    }
}
