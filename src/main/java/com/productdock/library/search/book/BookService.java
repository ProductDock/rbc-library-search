package com.productdock.library.search.book;

import com.productdock.library.search.elastic.RecordDocumentMapper;
import com.productdock.library.search.elastic.SearchQueryExecutor;
import com.productdock.library.search.elastic.document.BookDocument;
import com.productdock.library.search.kafka.cosumer.messages.BookAvailabilityMessage;
import com.productdock.library.search.kafka.cosumer.messages.RentalMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
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
        BookDocument bookDocument = getBookDocument(rentalMessage.getBookId());
        bookDocument.getBookStatusWrapper().setRecords(recordDocumentMapper.toRecords(rentalMessage.getRecords()));
        bookDocumentRepository.save(bookDocument);
    }

    public void updateAvailabilityBookCount(BookAvailabilityMessage bookAvailabilityMessage) {
        BookDocument bookDocument = getBookDocument(bookAvailabilityMessage.getBookId());
        bookDocument.getBookStatusWrapper().setAvailableBooksCount(bookAvailabilityMessage.getAvailableBookCount());
        bookDocumentRepository.save(bookDocument);
    }

    private BookDocument getBookDocument(String bookId) {
        var bookDocument = bookDocumentRepository.findById(bookId);
        if (bookDocument.isEmpty()) {
            throw new RuntimeException("Book index for bookId not found.");
        }
        return bookDocument.get();
    }
}
