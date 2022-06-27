package com.productdock.library.search.clean;

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
public record SuggestBooksApi(SuggestBooksQuery suggestBooksQuery, BookSearchSuggestionDtoMapper bookSearchSuggestionDtoMapper) {

    @GetMapping
    public List<BookSearchSuggestionDto> suggestBooks(@RequestParam Optional<String> searchText) {
        log.debug("GET request received - api/search/suggestions?searchText={}", searchText);
        var bookSuggestions = suggestBooksQuery.searchBookSuggestions(SearchFilters.builder().searchText(searchText).build());
        return bookSuggestions.stream().map(bookSearchSuggestionDtoMapper::toBookSearchSuggestionDto).toList();
    }
}
