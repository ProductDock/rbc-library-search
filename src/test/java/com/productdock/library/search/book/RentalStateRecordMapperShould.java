package com.productdock.library.search.book;

import com.productdock.library.search.elastic.RentalStateRecordMapper;
import com.productdock.library.search.elastic.RentalStateRecordMapperImpl;
import com.productdock.library.search.kafka.consumer.messages.RentalMessage;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Collectors;

import static java.util.stream.Stream.of;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RentalStateRecordMapperImpl.class})
class RentalStateRecordMapperShould {

    @Autowired
    private RentalStateRecordMapper rentalStateRecordMapper;

    @Test
    void mapRentalMessageRecordToRentalStateRecord() {
        var rentalMessageRecord = RentalMessage.Record.builder()
                .patron("email1").status(BookStatus.AVAILABLE).build();

        var rentalStateRecord = rentalStateRecordMapper.toRecord(rentalMessageRecord);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(rentalMessageRecord.getPatron()).isEqualTo(rentalStateRecord.getEmail());
            softly.assertThat(rentalMessageRecord.getStatus()).isEqualTo(rentalStateRecord.getStatus());
        }
    }

    @Test
    void mapRentalMessageRecordsToRentalStateRecords() {
        var rentalMessageRecords = of(
                RentalMessage.Record.builder().patron("email1").status(BookStatus.AVAILABLE).build(),
                RentalMessage.Record.builder().patron("email2").status(BookStatus.RENTED).build()
        ).collect(Collectors.toList());

        var rentalStateRecords = rentalStateRecordMapper.toRecords(rentalMessageRecords);

        try (var softly = new AutoCloseableSoftAssertions()) {
            softly.assertThat(rentalStateRecords)
                    .extracting("email", "status")
                    .containsExactlyInAnyOrder(
                            tuple("email1", BookStatus.AVAILABLE),
                            tuple("email2", BookStatus.RENTED)
                    );
        }
    }

}
