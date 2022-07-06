package com.productdock.library.search.adapter.in.web;

import com.productdock.library.search.adapter.in.web.dto.BookSearchSuggestionDto;
import com.productdock.library.search.adapter.in.web.mapper.BookSearchSuggestionDtoMapper;
import com.productdock.library.search.application.port.in.GetSuggestedBooksQuery;
import com.productdock.library.search.domain.SearchFilters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/search/suggestions")
@Slf4j
public record SuggestBooksApi(GetSuggestedBooksQuery suggestBooksQuery, BookSearchSuggestionDtoMapper bookSearchSuggestionDtoMapper) {

    @GetMapping
    public List<BookSearchSuggestionDto> suggestBooks(@RequestParam Optional<String> searchText) {
        log.debug("GET request received - api/search/suggestions?searchText={}", searchText);
        var bookSuggestions = suggestBooksQuery.searchBookSuggestions(SearchFilters.builder().searchText(searchText).build());
        return bookSuggestions.stream().map(bookSearchSuggestionDtoMapper::toDto).toList();
    }
}
