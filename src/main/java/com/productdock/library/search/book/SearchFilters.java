package com.productdock.library.search.book;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Builder
public class SearchFilters {

    private boolean recommended;
    @Builder.Default
    private Optional<List<String>> topics = Optional.of(new ArrayList<>());
    private Optional<String> searchText;

}
