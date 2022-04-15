package com.productdock.library.search;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/search")
public record SearchApi(BookService bookService) {

    @GetMapping
    public SearchBooksResponse findBook(@RequestParam(required = false) Optional<List<String>> topics,
                                        @RequestParam int page) {
        return bookService.getBooks(topics, page);
    }
}
