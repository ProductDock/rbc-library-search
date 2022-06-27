package com.productdock.library.search.clean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/search")
@Slf4j
public record FindBooksApi(FindBooksQuery findBooksQuery, SearchBooksMapper searchBooksMapper) {

    @GetMapping
    public SearchBooksResponse findBook(@RequestParam(required = false) Optional<List<String>> topics,
                                        @RequestParam(required = false) boolean recommended,
                                        @RequestParam(required = false) Optional<String> searchText,
                                        @RequestParam int page) {
        log.debug("GET request received - api/search?page={}&topics={}&recommended={}&searchText={}", page, topics, recommended, searchText);
        var searchFilters = SearchFilters.builder()
                .topics(topics)
                .recommended(recommended)
                .searchText(searchText)
                .build();
        var searchBooksResult = findBooksQuery.getBooks(searchFilters, page);
        return searchBooksMapper.toSearchBooksResponse(searchBooksResult);
    }

}
