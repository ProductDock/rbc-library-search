package com.productdock.library.search.book;

import com.productdock.library.search.elastic.RentalStateRecordMapper;
import com.productdock.library.search.elastic.SearchQueryExecutor;
import com.productdock.library.search.elastic.document.BookDocument;
import com.productdock.library.search.kafka.consumer.messages.BookAvailabilityMessage;
import com.productdock.library.search.kafka.consumer.messages.BookRatingMessage;
import com.productdock.library.search.kafka.consumer.messages.RentalMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public record BookService(BookDocumentRepository bookDocumentRepository,
                          SearchQueryExecutor searchQueryExecutor,
                          BookMapper bookMapper,
                          RentalStateRecordMapper recordDocumentMapper) {


    public SearchBooksResponse getBooks(Optional<List<String>> topics, int page) {
        var hits = searchQueryExecutor.execute(topics, page);
        var bookHitsDto = hits.stream().map(hit -> bookMapper.toBookDto(hit.getContent())).toList();
        return new SearchBooksResponse(hits.getTotalHits(), bookHitsDto);
    }

    public void save(BookDocument bookDocument) {
        bookDocumentRepository.save(bookDocument);
    }

    public void updateBookRecords(RentalMessage rentalMessage) {
        updateBook(rentalMessage.getBookId(),
                state -> state.setRecords(recordDocumentMapper.toRecords(rentalMessage.getRentalRecords())));
    }

    public void updateAvailabilityBookCount(BookAvailabilityMessage bookAvailabilityMessage) {
        updateBook(bookAvailabilityMessage.getBookId(),
                state -> state.setAvailableBooksCount(bookAvailabilityMessage.getAvailableBookCount()));
    }

    public void updateBookRating(BookRatingMessage bookRatingMessage) {
        var bookDocument = getBookDocument(bookRatingMessage.getBookId());
        var bookRating = bookDocument.getRating();
        bookRating.setCount(bookRatingMessage.getRatingsCount());
        bookRating.setScore(bookRatingMessage.getRating());
        bookDocumentRepository.save(bookDocument);
    }

    private void updateBook(String bookId, Consumer<BookDocument.RentalState> updater) {
        var bookDocument = getBookDocument(bookId);
        var bookRentalState = bookDocument.getRentalState();
        updater.accept(bookRentalState);
        bookDocumentRepository.save(bookDocument);
    }

    private BookDocument getBookDocument(String bookId) {
        return bookDocumentRepository.findById(bookId).orElseThrow();
    }

}
