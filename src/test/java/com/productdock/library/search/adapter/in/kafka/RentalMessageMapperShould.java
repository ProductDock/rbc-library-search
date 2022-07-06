package com.productdock.library.search.adapter.in.kafka;

import com.productdock.library.search.adapter.in.kafka.mappers.RentalMessageMapper;
import com.productdock.library.search.adapter.in.kafka.messages.RentalMessage;
import com.productdock.library.search.domain.BookStatus;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

import static java.util.stream.Stream.of;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

class RentalMessageMapperShould {

    private RentalMessageMapper rentalMessageMapper = Mappers.getMapper(RentalMessageMapper.class);

    @Test
    void mapRentalMessageRecordToBookRecord() {
        var rentalMessageRecord = RentalMessage.Record.builder()
                .patron("email1").status(BookStatus.AVAILABLE).build();

        var bookRecord = rentalMessageMapper.toRecord(rentalMessageRecord);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(bookRecord.getEmail()).isEqualTo(rentalMessageRecord.getPatron());
            softly.assertThat(bookRecord.getStatus()).isEqualTo(rentalMessageRecord.getStatus());
        }
    }

    @Test
    void mapRentalMessageRecordsToBookRecords() {
        var rentalMessageRecords = of(
                RentalMessage.Record.builder().patron("email1").status(BookStatus.AVAILABLE).build(),
                RentalMessage.Record.builder().patron("email2").status(BookStatus.RENTED).build()
        ).collect(Collectors.toList());

        var bookRecords = rentalMessageMapper.toRecords(rentalMessageRecords);

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
