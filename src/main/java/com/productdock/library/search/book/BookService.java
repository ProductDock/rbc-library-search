package com.productdock.library.search.book;

import com.productdock.library.search.elastic.SearchQueryExecutor;
import com.productdock.library.search.elastic.document.BookDocument;
import com.productdock.library.search.elastic.document.RecordMapper;
import com.productdock.library.search.kafka.cosumer.messages.RentalMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public record BookService(BookDocumentRepository bookDocumentRepository,
                          SearchQueryExecutor searchQueryExecutor,
                          BookMapper bookMapper,
                          RecordMapper recordMapper) {


    public SearchBooksResponse getBooks(Optional<List<String>> topics, int page) {
        var hits = searchQueryExecutor.execute(topics, page);
        var bookHitsDto = hits.stream().map(hit -> bookMapper.toBookDto(hit.getContent())).toList();
        return new SearchBooksResponse(hits.getTotalHits(), bookHitsDto);
    }

    public void save(BookDocument bookDocument) {
        bookDocumentRepository.save(bookDocument);
    }

    public void update(RentalMessage rentalMessage) {
        var bookDocument = bookDocumentRepository
                .findById(rentalMessage.getBookId());
        if (bookDocument.isPresent()){
            var book = bookDocument.get();
            book.setRecords(recordMapper.toRecords(rentalMessage.getRecords()));
            bookDocumentRepository.save(book);
        }
    }
}
