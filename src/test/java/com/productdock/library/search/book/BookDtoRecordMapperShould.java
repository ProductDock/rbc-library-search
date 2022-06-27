package com.productdock.library.search.book;


import com.productdock.library.search.clean.BookRecordMapper;
import com.productdock.library.search.clean.BookStatus;
import com.productdock.library.search.elastic.document.BookDocument;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

import static java.util.stream.Stream.of;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

class BookRecordMapperShould {

    private BookRecordMapper bookDtoRecordMapper = Mappers.getMapper(BookRecordMapper.class);

    @Test
    void mapRentalStateRecordToBookDtoRecord() {
        var rentalStateRecord = BookDocument.RentalState.Record.builder()
                .email("email1").status(BookStatus.AVAILABLE).build();

        var bookRecord = bookDtoRecordMapper.toRecord(rentalStateRecord);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookRecord.getEmail()).isEqualTo(rentalStateRecord.getEmail());
            softly.assertThat(bookRecord.getStatus()).isEqualTo(rentalStateRecord.getStatus());
        }
    }

    @Test
    void mapRentalStateRecordsToBookDtoRecords() {
        var rentalStateRecords = of(
                BookDocument.RentalState.Record.builder().email("email1").status(BookStatus.AVAILABLE).build(),
                BookDocument.RentalState.Record.builder().email("email2").status(BookStatus.RENTED).build()
        ).collect(Collectors.toList());

        var bookRecords = bookDtoRecordMapper.toRecords(rentalStateRecords);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookRecords)
                    .extracting("email", "status")
                    .containsExactlyInAnyOrder(
                            tuple("email1", BookStatus.AVAILABLE),
                            tuple("email2", BookStatus.RENTED)
                    );
        }
    }
}
