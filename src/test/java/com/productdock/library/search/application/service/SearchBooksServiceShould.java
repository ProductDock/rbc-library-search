package com.productdock.library.search.application.service;

import com.productdock.library.search.application.port.out.persistence.BookDocumentPersistenceOutPort;
import com.productdock.library.search.domain.SearchFilters;
import com.productdock.library.search.domain.SearchBooksResultsPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class SearchBooksServiceShould {

    private static final Optional<List<String>> ANY_TOPIC = Optional.of(List.of("TOPIC"));
    private static final boolean RECOMMENDED = false;
    private static final Optional<String> SEARCH_TEXT = Optional.of("");
    private static final int FIRST_PAGE = 0;
    private static final int BOOK_COUNT = 2;

    @InjectMocks
    private SearchBooksService searchBooksService;

    @Mock
    private BookDocumentPersistenceOutPort bookRepository;

    @Test
    void getBooksByTopics() {
        var searchFilters = SearchFilters.builder().recommended(RECOMMENDED).topics(ANY_TOPIC).searchText(SEARCH_TEXT).build();
        var searchResult = SearchBooksResultsPage.builder().count(BOOK_COUNT).books(List.of()).build();
        given(bookRepository.searchBooksBy(searchFilters, FIRST_PAGE)).willReturn(searchResult);

        var searchBooksResult = searchBooksService.searchBooks(searchFilters, FIRST_PAGE);

        assertThat(searchBooksResult.count).isEqualTo(2);
    }
}
