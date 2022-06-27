package com.productdock.library.search.clean;

import com.productdock.library.search.clean.Book;
import com.productdock.library.search.clean.BookRecordMapper;
import com.productdock.library.search.clean.BookStatus;
import com.productdock.library.search.elastic.document.BookDocument;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Stream.concat;

@Component
@AllArgsConstructor
@Slf4j
public class BookMapper {

    private BookRecordMapper recordMapper;

    public Book toBook(BookDocument bookDocument) {
        log.debug("Map BookDocument [{}] to BookDto", bookDocument);
        var book = new Book();
        mapSimpleProperties(bookDocument, book);
        mapRecords(bookDocument, book);
        mapRating(bookDocument, book);
        return book;
    }

    private void mapRating(BookDocument bookDocument, Book book) {
        log.debug("Map BookDocument rating {} to BookDto rating", bookDocument.getRating());
        if (bookDocument.getRating() != null){
            book.getRating().setScore(bookDocument.getRating().getScore());
            book.getRating().setCount(bookDocument.getRating().getCount());
        }
    }

    private void mapRecords(BookDocument bookDocument, Book book) {
        log.debug("Map BookDocument records [{}] to BookDto records", bookDocument.getRentalState().getRecords());
        var records = bookDocument.getRentalState().getRecords();
        var availableBookCount = bookDocument.getRentalState().getAvailableBooksCount();
        var availableRecords = createAvailableRecords(availableBookCount);
        var allRecords = concat(records.stream(), availableRecords.stream()).toList();
        book.setRecords(recordMapper.toRecords(allRecords));
    }

    private List<BookDocument.RentalState.Record> createAvailableRecords(int availableBookCount) {
        log.debug("Create {} available records", availableBookCount);
        var bookRecords = new ArrayList<BookDocument.RentalState.Record>();
        for (int i = 1; i <= availableBookCount; i++) {
            bookRecords.add(new BookDocument.RentalState.Record(BookStatus.AVAILABLE));
        }
        return bookRecords;
    }

    private void mapSimpleProperties(BookDocument bookDocument, Book book) {
        log.debug("Map simple properties from BookDocument [{}] to BookDto", bookDocument);
        book.setId(bookDocument.getBookId());
        book.setTitle(bookDocument.getTitle());
        book.setAuthor(bookDocument.getAuthor());
        book.setCover(bookDocument.getCover());
    }
}