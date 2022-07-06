package com.productdock.library.search.adapter.out.elastic;

import com.productdock.library.search.adapter.out.elastic.query.SearchQueryBuilder;
import com.productdock.library.search.adapter.out.elastic.query.SearchQueryExecutor;
import com.productdock.library.search.domain.Book;
import com.productdock.library.search.domain.SearchFilters;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookElasticRepositoryShould {

    private static final String BOOK_ID = "1";
    private static final int PAGE = 0;
    private static final Optional<BookDocument> OPTIONAL_BOOK_DOCUMENT = Optional.of(mock(BookDocument.class));
    private static final Book BOOK = mock(Book.class);
    private static final BookDocument BOOK_DOCUMENT = mock(BookDocument.class);
    private static final SearchFilters SEARCH_FILTERS = mock(SearchFilters.class);
    private static final BoolQueryBuilder QUERY_BUILDER = mock(BoolQueryBuilder.class);
    private static final SearchHits<BookDocument> HITS = mock(SearchHits.class);
    private static final SearchHit<BookDocument> HIT = mock(SearchHit.class);

    @InjectMocks
    private BookElasticRepository bookElasticRepository;

    @Mock
    private BookDocumentElasticRepository bookDocumentElasticRepository;

    @Mock
    private BookDocumentMapper bookDocumentMapper;

    @Mock
    private SearchQueryBuilder searchQueryBuilder;

    @Mock
    private SearchQueryExecutor searchQueryExecutor;

    @Test
    void findBookByIdWhenBookExists() {
        given(bookDocumentElasticRepository.findById(BOOK_ID)).willReturn(OPTIONAL_BOOK_DOCUMENT);
        given(bookDocumentMapper.toDomain(OPTIONAL_BOOK_DOCUMENT.get())).willReturn(BOOK);

        var book = bookElasticRepository.findById(BOOK_ID);

        assertThat(book).isPresent();
    }

    @Test
    void notFindBookByIdWhenBookDoesNotExist() {
        given(bookDocumentElasticRepository.findById(BOOK_ID)).willReturn(Optional.empty());

        var book = bookElasticRepository.findById(BOOK_ID);

        assertThat(book).isEmpty();
    }

    @Test
    void saveBookDocument() {
        given(bookDocumentMapper.toEntity(BOOK)).willReturn(BOOK_DOCUMENT);

        bookElasticRepository.save(BOOK);

        verify(bookDocumentElasticRepository).save(BOOK_DOCUMENT);
    }

    @Test
    void searchBooksWithoutPagination() {
        given(searchQueryBuilder.buildWith(SEARCH_FILTERS)).willReturn(QUERY_BUILDER);
        given(searchQueryExecutor.execute(QUERY_BUILDER)).willReturn(HITS);
        given(HITS.stream()).willReturn(Stream.of(HIT, HIT));
        given(bookDocumentMapper.toDomain(HIT.getContent())).willReturn(BOOK);

        var books = bookElasticRepository.searchBooksBy(SEARCH_FILTERS);

        assertThat(books).hasSize(2);
    }

    @Test
    void searchBooksWithPagination() {
        given(searchQueryBuilder.buildWith(SEARCH_FILTERS)).willReturn(QUERY_BUILDER);
        given(searchQueryExecutor.execute(QUERY_BUILDER, PAGE)).willReturn(HITS);
        given(HITS.stream()).willReturn(Stream.of(HIT, HIT));
        given(HITS.getTotalHits()).willReturn(2L);
        given(bookDocumentMapper.toDomain(HIT.getContent())).willReturn(BOOK);

        var searchBooksResult = bookElasticRepository.searchBooksBy(SEARCH_FILTERS, PAGE);

        assertThat(searchBooksResult.count).isEqualTo(2);
        assertThat(searchBooksResult.books).hasSize(2);
    }
}
