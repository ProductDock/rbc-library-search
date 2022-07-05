package com.productdock.library.search.adapter.in.web.mapper;

import com.productdock.library.search.adapter.in.web.dto.BookDto;
import com.productdock.library.search.adapter.in.web.dto.SearchBooksResponse;
import com.productdock.library.search.domain.Book;
import com.productdock.library.search.domain.BookStatus;
import com.productdock.library.search.domain.SearchBooksResultsPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Stream.concat;

@Component
@AllArgsConstructor
@Slf4j
public class SearchBooksMapper {

    private BookRecordMapper recordMapper;

    public SearchBooksResponse toResponse(SearchBooksResultsPage searchBooksResult) {
        log.debug("Map SearchBooksResult [{}] to SearchBooksResponse", searchBooksResult);
        var searchBooksResponse = new SearchBooksResponse();
        searchBooksResponse.count = searchBooksResult.count;
        for (Book book: searchBooksResult.books) {
            searchBooksResponse.books.add(generateBookDto(book));
        }
        return searchBooksResponse;
    }

    private BookDto generateBookDto(Book book) {
        var bookDto = new BookDto();
        mapSimpleProperties(book, bookDto);
        mapRecords(book, bookDto);
        mapRating(book, bookDto);
        return bookDto;
    }

    private void mapRating(Book book, BookDto bookDto) {
        log.debug("Map Book rating {} to BookDto rating", book.getRating());
        if (book.getRating() != null){
            bookDto.rating.score = book.getRating().getScore();
            bookDto.rating.count = book.getRating().getCount();
        }
    }

    private void mapRecords(Book book, BookDto bookDto) {
        log.debug("Map BookDocument records [{}] to BookDto records", book.getRentalState().getRecords());
        var records = book.getRentalState().getRecords();
        var availableBookCount = book.getRentalState().getAvailableBooksCount();
        var availableRecords = createAvailableRecords(availableBookCount);
        var allRecords = concat(records.stream(), availableRecords.stream()).toList();
        bookDto.records = recordMapper.toRecords(allRecords);
    }

    private List<Book.RentalState.Record> createAvailableRecords(int availableBookCount) {
        log.debug("Create {} available records", availableBookCount);
        var bookRecords = new ArrayList<Book.RentalState.Record>();
        for (int i = 1; i <= availableBookCount; i++) {
            bookRecords.add(new Book.RentalState.Record(BookStatus.AVAILABLE));
        }
        return bookRecords;
    }

    private void mapSimpleProperties(Book book, BookDto bookDto) {
        log.debug("Map simple properties from Book [{}] to BookDto", book);
        bookDto.id = book.getId();
        bookDto.title = book.getTitle();
        bookDto.author = book.getAuthor();
        bookDto.cover = book.getCover();
    }
}
