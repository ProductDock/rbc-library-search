package com.productdock.library.search.book;

import com.productdock.library.search.elastic.RecordDocumentMapper;
import com.productdock.library.search.elastic.SearchQueryExecutor;
import com.productdock.library.search.elastic.document.BookDocument;
import com.productdock.library.search.elastic.document.BookStatusWrapper;
import com.productdock.library.search.kafka.cosumer.messages.BookAvailabilityMessage;
import com.productdock.library.search.kafka.cosumer.messages.RentalMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public record BookService(BookDocumentRepository bookDocumentRepository,
                          SearchQueryExecutor searchQueryExecutor,
                          BookMapper bookMapper,
                          RecordDocumentMapper recordDocumentMapper) {


    public SearchBooksResponse getBooks(Optional<List<String>> topics, int page) {
        var hits = searchQueryExecutor.execute(topics, page);
        var bookHitsDto = hits.stream().map(hit -> bookMapper.toBookDto(hit.getContent())).toList();
        return new SearchBooksResponse(hits.getTotalHits(), bookHitsDto);
    }

    public void save(BookDocument bookDocument) {
        bookDocumentRepository.save(bookDocument);
    }

    public void updateBookRecords(RentalMessage rentalMessage) {
        var bookDocument = getBookDocument(rentalMessage.getBookId());
        var bookStatusWrapper = getOrCreateBookStatusWrapper(bookDocument);
        bookStatusWrapper.setRecords(recordDocumentMapper.toRecords(rentalMessage.getRecords()));
        bookDocument.setBookStatusWrapper(bookStatusWrapper);
        bookDocumentRepository.save(bookDocument);
    }

    public void updateAvailabilityBookCount(BookAvailabilityMessage bookAvailabilityMessage) {
        var bookDocument = getBookDocument(bookAvailabilityMessage.getBookId());
        var bookStatusWrapper = getOrCreateBookStatusWrapper(bookDocument);
        bookStatusWrapper.setAvailableBooksCount(bookAvailabilityMessage.getAvailableBookCount());
        bookDocument.setBookStatusWrapper(bookStatusWrapper);
        bookDocumentRepository.save(bookDocument);
    }

    private BookDocument getBookDocument(String bookId) {
        var bookDocument = bookDocumentRepository.findById(bookId);
        if (bookDocument.isEmpty()) {
            throw new RuntimeException("Book index for bookId not found.");
        }
        return bookDocument.get();
    }

    private BookStatusWrapper getOrCreateBookStatusWrapper(BookDocument bookDocument) {
        if (bookDocument.getBookStatusWrapper() == null) {
            return new BookStatusWrapper(0, new ArrayList<>());
        }
        return bookDocument.getBookStatusWrapper();
    }
}
