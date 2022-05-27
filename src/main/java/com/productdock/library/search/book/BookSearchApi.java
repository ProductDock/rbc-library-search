package com.productdock.library.search.book;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/search")
public record BookSearchApi(BookService bookService) {

    @GetMapping
    public SearchBooksResponse findBook(@RequestParam(required = false) Optional<List<String>> topics,
                                        @RequestParam(required = false) boolean recommended,
                                        @RequestParam int page) {
        return bookService.getBooks(new SearchFilters(page, recommended, topics));
    }
}
