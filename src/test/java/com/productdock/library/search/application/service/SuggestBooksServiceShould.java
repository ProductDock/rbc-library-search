package com.productdock.library.search.application.service;

import com.productdock.library.search.application.port.out.persistence.BookPersistenceOutPort;
import com.productdock.library.search.domain.Book;
import com.productdock.library.search.domain.SearchFilters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class SuggestBooksServiceShould {

    private static final Optional<String> SEARCH_TEXT = Optional.of("SEARCH");

    @InjectMocks
    private SuggestBooksService suggestBooksService;

    @Mock
    private BookPersistenceOutPort bookRepository;

    @Test
    void getBooksByTopics() {
        var searchFilters = SearchFilters.builder().searchText(SEARCH_TEXT).build();
        var books = List.of(mock(Book.class) , mock(Book.class));
        given(bookRepository.searchBooksBy(searchFilters)).willReturn(books);

        var searchBooksResult = suggestBooksService.searchBookSuggestions(searchFilters);

        assertThat(searchBooksResult).hasSize(2);
    }

}
