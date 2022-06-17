package com.productdock.library.search.book;

import com.productdock.library.search.elastic.BookRatingMapper;
import com.productdock.library.search.elastic.RentalStateRecordMapper;
import com.productdock.library.search.elastic.SearchQueryExecutor;
import com.productdock.library.search.elastic.document.BookDocument;
import com.productdock.library.search.kafka.consumer.messages.BookAvailabilityMessage;
import com.productdock.library.search.kafka.consumer.messages.BookRatingMessage;
import com.productdock.library.search.kafka.consumer.messages.BookRecommendationMessage;
import com.productdock.library.search.kafka.consumer.messages.RentalMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
@Slf4j
public record BookService(BookDocumentRepository bookDocumentRepository,
                          SearchQueryExecutor searchQueryExecutor,
                          BookSearchSuggestionDtoMapper bookSearchSuggestionDtoMapper,
                          BookMapper bookMapper,
                          BookRatingMapper bookRatingMapper,
                          RentalStateRecordMapper recordDocumentMapper) {


    public SearchBooksResponse getBooks(SearchFilters searchFilters) {
        log.debug("Get books by search filters: {}", searchFilters);
        var hits = searchQueryExecutor.execute(searchFilters);
        var bookHitsDto = hits.stream().map(hit -> bookMapper.toBookDto(hit.getContent())).toList();
        return new SearchBooksResponse(hits.getTotalHits(), bookHitsDto);
    }

    public void save(BookDocument bookDocument) {
        bookDocumentRepository.save(bookDocument);
    }

    public void updateBookRecords(RentalMessage rentalMessage) {
        log.debug("For book with id {} update records by rental message: {}", rentalMessage.getBookId(), rentalMessage);
        updateBook(rentalMessage.getBookId(),
                state -> state.getRentalState().setRecords(recordDocumentMapper.toRecords(rentalMessage.getRentalRecords())));
    }

    public void updateAvailabilityBookCount(BookAvailabilityMessage bookAvailabilityMessage) {
        log.debug("For book with id {} update availability book count by message: {}", bookAvailabilityMessage.getBookId(), bookAvailabilityMessage);
        updateBook(bookAvailabilityMessage.getBookId(),
                state -> state.getRentalState().setAvailableBooksCount(bookAvailabilityMessage.getAvailableBookCount()));
    }

    public void updateBookRating(BookRatingMessage bookRatingMessage) {
        log.debug("For book with id {} update book rating by message: {}", bookRatingMessage.getBookId(), bookRatingMessage);
        updateBook(bookRatingMessage.getBookId(),
                state -> state.setRating(bookRatingMapper.toRating(bookRatingMessage)));
    }

    public void updateBookRecommendations(BookRecommendationMessage bookRecommendationMessage) {
        log.debug("For book with id {} update book recommendation by message: {}", bookRecommendationMessage.getBookId(), bookRecommendationMessage);
        updateBook(bookRecommendationMessage.getBookId(),
                state -> state.setRecommended(bookRecommendationMessage.getRecommended()));
    }

    private void updateBook(String bookId, Consumer<BookDocument> updater) {
        log.debug("Update book with id: {}", bookId);
        var bookDocument = getBookDocument(bookId);
        updater.accept(bookDocument);
        bookDocumentRepository.save(bookDocument);
    }

    private BookDocument getBookDocument(String bookId) {
        log.debug("Find book with id: {}", bookId);
        return bookDocumentRepository.findById(bookId).orElseThrow();
    }

    public List<BookSearchSuggestionDto> searchBooksByText(SearchFilters searchFilters) {
        log.debug("Get books by search text: {}", searchFilters.getSearchText());
        var hits = searchQueryExecutor.execute(searchFilters);
        return hits.stream().map(hit -> bookSearchSuggestionDtoMapper.toBookSearchSuggestionDto(hit.getContent())).toList();
    }
}
