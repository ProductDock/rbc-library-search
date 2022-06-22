package com.productdock.library.search.book;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/search")
@Slf4j
public record BookSearchApi(BookService bookService) {

    @GetMapping
    public SearchBooksResponse findBook(@RequestParam(required = false) Optional<List<String>> topics,
                                        @RequestParam(required = false) boolean recommended,
                                        @RequestParam(required = false) Optional<String> searchText,
                                        @RequestParam int page) {
        log.debug("GET request received - api/search?page={}&topics={}&recommended={}&searchText={}", page, topics, recommended, searchText);
        return bookService.getBooks(SearchFilters.builder().topics(topics).recommended(recommended).searchText(searchText).build(), page);
    }

    @GetMapping("/suggestions")
    public List<BookSearchSuggestionDto> suggestBooks(@RequestParam Optional<String> searchText) {
        log.debug("GET request received - api/search/suggestions?searchText={}", searchText);
        return bookService.searchBookSuggestions(SearchFilters.builder().searchText(searchText).build());
    }
}
