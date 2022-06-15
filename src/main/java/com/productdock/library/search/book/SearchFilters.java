package com.productdock.library.search.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchFilters {

    private int page;
    private boolean recommended;
    private Optional<List<String>> topics;
    private String searchText;
}
