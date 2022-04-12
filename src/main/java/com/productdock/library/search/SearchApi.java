package com.productdock.library.search;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/search")
public record SearchApi(BookIndexRepository bookRepository, BookService bookService) {

    @GetMapping
    public CountableCollectionDto findBook(@RequestParam(required = false) Optional<List<String>> topics,
                                           @RequestParam int page) {
        return bookService.getBooks(topics, page);
    }
}
