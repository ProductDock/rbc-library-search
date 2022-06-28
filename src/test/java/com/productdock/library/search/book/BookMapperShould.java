//package com.productdock.library.search.book;
//
//import com.productdock.library.search.adapter.out.elastic.BookMapper;
//import com.productdock.library.search.clean.BookRecordMapperImpl;
//import com.productdock.library.search.domain.BookStatus;
//import org.assertj.core.api.AutoCloseableSoftAssertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import static com.productdock.library.search.data.provider.BookDocumentMother.defaultBookDocumentBuilder;
//import static com.productdock.library.search.data.provider.BookDocumentRentalStateMother.defaultRentalStateBuilder;
//import static com.productdock.library.search.data.provider.BookDocumentRentalStateRecordMother.defaultRecord;
//import static org.assertj.core.api.AssertionsForClassTypes.tuple;
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {BookMapper.class, BookRecordMapperImpl.class})
//class BookMapperShould {
//
//    @Autowired
//    private BookMapper bookMapper;
//
//    @Test
//    void mapBookDocumentToBookDto() {
//        var bookDocument = defaultBookDocumentBuilder()
//                .rentalState(defaultRentalStateBuilder()
//                        .record(defaultRecord()).build()).build();
//
//        var book = bookMapper.toBook(bookDocument);
//
//        try (var softly = new AutoCloseableSoftAssertions()) {
//            softly.assertThat(book.getId()).isEqualTo(bookDocument.getBookId());
//            softly.assertThat(book.getTitle()).isEqualTo(bookDocument.getTitle());
//            softly.assertThat(book.getAuthor()).isEqualTo(bookDocument.getAuthor());
//            softly.assertThat(book.getCover()).isEqualTo(bookDocument.getCover());
//            softly.assertThat(book.getRating().getCount()).isEqualTo(bookDocument.getRating().getCount());
//            softly.assertThat(book.getRating().getScore()).isEqualTo(bookDocument.getRating().getScore());
//            softly.assertThat(book.getRecords())
//                    .extracting("email", "status")
//                    .containsExactly(tuple("::email::", BookStatus.RENTED));
//        }
//    }
//
//    @Test
//    void mapBookDocumentToBookDto_whenRatingNotPresent() {
//        var bookDocument = defaultBookDocumentBuilder()
//                .rating(null)
//                .build();
//
//        var book = bookMapper.toBook(bookDocument);
//
//        try (var softly = new AutoCloseableSoftAssertions()) {
//            softly.assertThat(book.getRating().getCount()).isNull();
//            softly.assertThat(book.getRating().getScore()).isNull();
//        }
//    }
//
//    @Test
//    void mapBookDocumentToBookDto_whenBookStatusWrapperHasOneRecordAndOneAvailableBookCount() {
//        var bookDocument =
//                defaultBookDocumentBuilder()
//                        .bookId("123")
//                        .cover("Book cover")
//                        .rentalState(defaultRentalStateBuilder()
//                                .availableBooksCount(1)
//                                .record(defaultRecord())
//                                .build()).build();
//
//        var book = bookMapper.toBook(bookDocument);
//
//        try (var softly = new AutoCloseableSoftAssertions()) {
//            softly.assertThat(book.getId()).isEqualTo(bookDocument.getBookId());
//            softly.assertThat(book.getTitle()).isEqualTo(bookDocument.getTitle());
//            softly.assertThat(book.getAuthor()).isEqualTo(bookDocument.getAuthor());
//            softly.assertThat(book.getCover()).isEqualTo(bookDocument.getCover());
//            softly.assertThat(book.getRecords()).hasSize(2);
//            softly.assertThat(book.getRecords())
//                    .extracting("email", "status")
//                    .containsExactlyInAnyOrder(
//                            tuple(null, BookStatus.AVAILABLE),
//                            tuple("::email::", BookStatus.RENTED)
//                    );
//        }
//    }
//}
