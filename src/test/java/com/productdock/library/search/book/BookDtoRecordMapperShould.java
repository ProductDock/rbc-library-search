package com.productdock.library.search.book;


import com.productdock.library.search.elastic.document.BookDocument;
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
@ContextConfiguration(classes = {BookDtoRecordMapperImpl.class})
class BookDtoRecordMapperShould {
//
//    @Autowired
//    private BookDtoRecordMapper bookDtoRecordMapper;
//
//    @Test
//    void mapRentalStateRecordToBookDtoRecord() {
//        var rentalStateRecord = BookDocument.RentalState.Record.builder()
//                .email("email1").status(BookStatus.AVAILABLE).build();
//
//        var bookDtoRecord = bookDtoRecordMapper.toRecordDto(rentalStateRecord);
//
//        try (var softly = new AutoCloseableSoftAssertions()) {
//            softly.assertThat(bookDtoRecord.email).isEqualTo(rentalStateRecord.getEmail());
//            softly.assertThat(bookDtoRecord.status).isEqualTo(rentalStateRecord.getStatus());
//        }
//    }
//
//    @Test
//    void mapRentalStateRecordsToBookDtoRecords() {
//        var rentalStateRecords = of(
//                BookDocument.RentalState.Record.builder().email("email1").status(BookStatus.AVAILABLE).build(),
//                BookDocument.RentalState.Record.builder().email("email2").status(BookStatus.RENTED).build()
//        ).collect(Collectors.toList());
//
//        var bookDtoRecords = bookDtoRecordMapper.toRecordsDto(rentalStateRecords);
//
//        try (var softly = new AutoCloseableSoftAssertions()) {
//            softly.assertThat(bookDtoRecords)
//                    .extracting("email", "status")
//                    .containsExactlyInAnyOrder(
//                            tuple("email1", BookStatus.AVAILABLE),
//                            tuple("email2", BookStatus.RENTED)
//                    );
//        }
//    }
}
