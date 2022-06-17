package com.productdock.library.search.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchFilters {

    private Integer page = null;
    private boolean recommended = false;
    private Optional<List<String>> topics = Optional.of(new ArrayList<>());
    @With
    private Optional<String> searchText;

}
