package com.productdock.library.search.adapter.out.elastic;

import com.productdock.library.search.adapter.out.elastic.query.SearchQueryBuilder;
import com.productdock.library.search.adapter.out.elastic.query.SearchQueryExecutor;
import com.productdock.library.search.application.port.out.persistence.BookDocumentPersistenceOutPort;
import com.productdock.library.search.domain.Book;
import com.productdock.library.search.domain.SearchFilters;
import com.productdock.library.search.domain.SearchBooksResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@AllArgsConstructor
public class BookDocumentRepository implements BookDocumentPersistenceOutPort {

    private BookDocumentElasticRepository entityRepository;
    private BookDocumentMapper mapper;
    private SearchQueryBuilder queryBuilder;
    private SearchQueryExecutor queryExecutor;

    @Override
    public Optional<Book> findById(String bookId) {
        var entity = entityRepository.findById(bookId);
        if(entity.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(mapper.toDomain(entity.get()));
    }

    @Override
    public void save(Book book) {
        var bookDocument = mapper.toEntity(book);
        entityRepository.save(bookDocument);
    }

    @Override
    public List<Book> searchBooksBy(SearchFilters searchFilters) {
        var query = queryBuilder.buildWith(searchFilters);
        var hits = queryExecutor.execute(query);
        return hits.stream().map(hit -> mapper.toDomain(hit.getContent())).toList();
    }

    @Override
    public SearchBooksResult searchBooksBy(SearchFilters searchFilters, int page) {
        var query = queryBuilder.buildWith(searchFilters);
        var hits = queryExecutor.execute(query, page);
        var books = hits.stream().map(hit -> mapper.toDomain(hit.getContent())).toList();
        return new SearchBooksResult(hits.getTotalHits(), books);
    }
}
