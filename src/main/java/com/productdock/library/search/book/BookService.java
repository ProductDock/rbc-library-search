package com.productdock.library.search.book;

import com.productdock.library.search.elastic.BookRatingMapper;
import com.productdock.library.search.elastic.RentalStateRecordMapper;
import com.productdock.library.search.elastic.SearchQueryExecutor;
import com.productdock.library.search.elastic.document.BookDocument;
import com.productdock.library.search.kafka.consumer.messages.BookAvailabilityMessage;
import com.productdock.library.search.kafka.consumer.messages.BookRatingMessage;
import com.productdock.library.search.kafka.consumer.messages.BookRecommendedMessage;
import com.productdock.library.search.kafka.consumer.messages.RentalMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public record BookService(BookDocumentRepository bookDocumentRepository,
                          SearchQueryExecutor searchQueryExecutor,
                          BookMapper bookMapper,
                          BookRatingMapper bookRatingMapper,
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
                state -> state.getRentalState().setRecords(recordDocumentMapper.toRecords(rentalMessage.getRentalRecords())));
    }

    public void updateAvailabilityBookCount(BookAvailabilityMessage bookAvailabilityMessage) {
        updateBook(bookAvailabilityMessage.getBookId(),
                state -> state.getRentalState().setAvailableBooksCount(bookAvailabilityMessage.getAvailableBookCount()));
    }

    public void updateBookRating(BookRatingMessage bookRatingMessage) {
        updateBook(bookRatingMessage.getBookId(),
                state -> state.setRating(bookRatingMapper.toRating(bookRatingMessage)));
    }

    public void updateBookRecommendations(BookRecommendedMessage bookRecommendedMessage) {
        updateBook(bookRecommendedMessage.getBookId(), state -> state.setRecommended(true));
    }

    private void updateBook(String bookId, Consumer<BookDocument> updater) {
        var bookDocument = getBookDocument(bookId);
        updater.accept(bookDocument);
        bookDocumentRepository.save(bookDocument);
    }

    private BookDocument getBookDocument(String bookId) {
        return bookDocumentRepository.findById(bookId).orElseThrow();
    }

}
