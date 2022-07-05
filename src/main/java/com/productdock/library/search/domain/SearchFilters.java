package com.productdock.library.search.domain;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Builder
public class SearchFilters {

    private final boolean recommended;
    @Builder.Default
    private final Optional<List<String>> topics = Optional.of(new ArrayList<>());
    private final Optional<String> searchText;

}
